package demo.user.service;


import com.google.common.base.Joiner;
import com.google.common.hash.Hashing;
import core.framework.database.JPAAccess;
import core.framework.database.Query;
import demo.user.domain.User;
import demo.user.domain.UserStatus;
import demo.user.web.user.CreateUserRequest;
import demo.user.web.user.LoginRequest;
import demo.web.BadRequestException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
@Component
public class UserService {
    @Inject
    JPAAccess repository;

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


    @Transactional
    public User create(CreateUserRequest request) {
        if (isUsernameExists(request.username)) {
            throw new BadRequestException("user.duplicatedUsername");
        }
        if (isEmailExits(request.email)) {
            throw new BadRequestException("user.duplicatedEmail");
        }

        User user = new User();
        user.id = UUID.randomUUID().toString();
        user.username = request.username;
        user.email = request.email;
        user.roleIds = request.roleIds == null ? null : Joiner.on(';').join(request.roleIds);
        user.salt = salt();
        user.iteration = (int) (System.currentTimeMillis() % 4 + 1);
        user.hashedPassword = new PasswordHasher(user.salt, request.password).hash(user.iteration);
        user.status = UserStatus.ACTIVE;
        user.createdBy = request.requestBy;
        user.updatedBy = request.requestBy;
        user.createdTime = OffsetDateTime.now();
        user.updatedTime = OffsetDateTime.now();
        repository.save(user);
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
        List<User> users = repository.find(Query.create("SELECT t FROM User t WHERE t.username=:username").param("username", username.toLowerCase(Locale.ENGLISH)));
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    public Optional<User> findByEmail(String email) {
        List<User> users = repository.find(Query.create("SELECT t FROM User t WHERE t.email=:email").param("email", email.toLowerCase(Locale.ENGLISH)));
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }
}
