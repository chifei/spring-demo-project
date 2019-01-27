package app.demo.user.service;


import app.demo.common.database.JpaRepository;
import app.demo.common.database.Query;
import app.demo.user.domain.UserRole;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;


@Component
public class UserRoleService {
    @Inject
    JpaRepository repository;

    @Transactional
    public UserRole create(String userId, String roleId) {
        UserRole userRole = new UserRole();
        userRole.id = UUID.randomUUID().toString();
        userRole.userId = userId;
        userRole.roleId = roleId;
        repository.save(userRole);
        return userRole;
    }

    public List<String> getRoleIds(String userId) {
        Query query = Query.create("SELECT t.roleId FROM UserRole t WHERE 1=1");
        query.append("AND t.userId=:userId");
        query.param("userId", userId);
        return repository.find(query);
    }

    @Transactional
    public void deleteByUserId(String userId) {
        Query query = Query.create("DELETE FROM UserRole t WHERE 1=1");
        query.append("AND t.userId=:userId");
        query.param("userId", userId);
        repository.update(query);
    }
}
