package app.demo.user.service;


import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import app.demo.common.database.JpaRepository;
import app.demo.common.database.Query;
import app.demo.common.exception.InvalidRequestException;
import app.demo.user.domain.User;
import app.demo.user.domain.UserStatus;
import app.demo.user.service.message.UserChangedMessagePublisher;
import app.demo.user.web.user.CreateUserRequest;
import app.demo.user.web.user.DeleteUserRequest;
import app.demo.user.web.user.LoginRequest;
import app.demo.user.web.user.UpdateUserPasswordRequest;
import app.demo.user.web.user.UpdateUserRequest;
import app.demo.user.web.user.UserQuery;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
public class UserService {
    @Inject
    UserRoleService userRoleService;

    @Inject
    JpaRepository repository;

    @Inject
    UserChangedMessagePublisher userChangedMessagePublisher;

    public List<User> batchGet(List<String> ids) {
        StringBuilder b = new StringBuilder(64);
        b.append("SELECT t FROM User t WHERE t.id in (");
        int maxGroupCount = 1000;
        for (int i = 0; i < ids.size(); i++) {
            int index = i % maxGroupCount;
            if (i != 0 && index == 0) {
                b.append(") OR t.id in (");
            } else if (index != 0) {
                b.append(',');
            }
            b.append('\'').append(ids.get(i)).append('\'');
        }
        b.append(')');
        return repository.find(Query.create(b.toString()));
    }

    @Transactional
    public void batchDelete(DeleteUserRequest request) {
        StringBuilder b = new StringBuilder(64);
        b.append("DELETE FROM User t WHERE t.id in (");
        int maxGroupCount = 1000;
        for (int i = 0; i < request.ids.size(); i++) {
            int index = i % maxGroupCount;
            if (i != 0 && index == 0) {
                b.append(") OR t.id in (");
            } else if (index != 0) {
                b.append(',');
            }
            b.append('\'').append(request.ids.get(i)).append('\'');
            userRoleService.deleteByUserId(request.ids.get(i));
        }
        b.append(')');
        repository.update(Query.create(b.toString()));

        List<User> users = batchGet(request.ids);
        for (User user : users) {
            user.status = UserStatus.INACTIVE;
            user.updatedTime = OffsetDateTime.now();
            user.updatedBy = request.requestBy;
            userChangedMessagePublisher.notifyUserChanged(user);
        }
    }

    public User login(LoginRequest request) {
        Optional<User> userOptional = findByUsername(request.username);
        if (!userOptional.isPresent()) {
            return null;
        }
        User user = userOptional.get();
        if (!user.hashedPassword.equals(new PasswordHasher(user.salt, request.password).hash(user.iteration))) {
            return null;
        }
        if (user.status == UserStatus.INACTIVE) {
            return null;
        }
        return userOptional.get();
    }

    public List<User> find(UserQuery userQuery) {
        Query query = Query.create("SELECT t FROM User t");
        if (!Strings.isNullOrEmpty(userQuery.roleId)) {
            query.append("JOIN UserRole r ON t.id = r.userId AND r.roleId=:roleId");
            query.param("roleId", userQuery.roleId);
        }
        if (!Strings.isNullOrEmpty(userQuery.username)) {
            query.append("WHERE t.username=:username");
            query.param("username", userQuery.username);
        }
        query.fetch(userQuery.limit);
        query.from(userQuery.limit * (userQuery.page - 1));
        return repository.find(query);
    }

    public long count(UserQuery userQuery) {
        Query query = Query.create("SELECT count(t) FROM User t");
        if (!Strings.isNullOrEmpty(userQuery.roleId)) {
            query.append("JOIN UserRole r ON t.id = r.userId AND r.roleId=:roleId");
            query.param("roleId", userQuery.roleId);
        }
        if (!Strings.isNullOrEmpty(userQuery.username)) {
            query.append("WHERE t.username=:username");
            query.param("username", userQuery.username);
        }
        query.fetch(userQuery.limit);
        query.from(userQuery.limit * (userQuery.page - 1));
        return repository.count(query);
    }

    @Transactional
    public User create(CreateUserRequest request) {
        if (isUsernameExists(request.username)) {
            throw new InvalidRequestException("user.username", "duplicated username");
        }
        if (isEmailExits(request.email)) {
            throw new InvalidRequestException("user.email", "duplicated email");
        }

        User user = new User();
        user.id = UUID.randomUUID().toString();
        user.username = request.username;
        user.email = request.email;
        user.salt = salt();
        user.iteration = (int) (System.currentTimeMillis() % 4 + 1);
        user.hashedPassword = new PasswordHasher(user.salt, request.password).hash(user.iteration);
        user.status = UserStatus.ACTIVE;
        user.createdBy = request.requestBy;
        user.updatedBy = request.requestBy;
        user.createdTime = OffsetDateTime.now();
        user.updatedTime = OffsetDateTime.now();
        repository.save(user);
        if (request.roleIds != null && !request.roleIds.isEmpty()) {
            for (String roleId : request.roleIds) {
                userRoleService.create(user.id, roleId);
            }
        }
        userChangedMessagePublisher.notifyUserChanged(user);
        return user;
    }

    @Transactional
    public User update(String id, UpdateUserRequest request) {
        User user = get(id);
        if (request.email != null) {
            Optional<User> duplicateEmailUser = findByEmail(request.email);
            if (duplicateEmailUser.isPresent() && !duplicateEmailUser.get().id.equals(id)) {
                throw new InvalidRequestException("user.email", "duplicated email");
            }
            user.email = request.email;
        }
        user.updatedTime = OffsetDateTime.now();
        user.updatedBy = request.requestBy;
        repository.update(user);
        if (request.roleIds != null && !request.roleIds.isEmpty()) {
            userRoleService.deleteByUserId(user.id);
            for (String roleId : request.roleIds) {
                userRoleService.create(user.id, roleId);
            }
        }
        userChangedMessagePublisher.notifyUserChanged(user);
        return user;
    }

    public User get(String id) {
        return repository.get(User.class, id);
    }

    public boolean isUsernameExists(String username) {
        return findByUsername(username).isPresent();
    }

    public boolean isEmailExits(String email) {
        return email == null || findByEmail(email).isPresent();
    }

    private String salt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return Hashing.sha256().hashBytes(bytes).toString();
    }

    public Optional<User> findByUsername(String username) {
        List<User> users = repository.find(Query.create("SELECT t FROM User t WHERE t.username=:username").param("username", username));
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    public Optional<User> findByEmail(String email) {
        List<User> users = repository.find(Query.create("SELECT t FROM User t WHERE t.email=:email").param("email", email));
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    @Transactional
    public void updatePassword(String id, UpdateUserPasswordRequest request) {
        User user = get(id);
        user.salt = salt();
        user.iteration = (int) (System.currentTimeMillis() % 4 + 1);
        user.hashedPassword = new PasswordHasher(user.salt, request.password).hash(user.iteration);
        user.updatedBy = request.requestBy;
        user.updatedTime = OffsetDateTime.now();
        repository.update(user);
    }
}
