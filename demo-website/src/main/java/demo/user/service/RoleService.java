package demo.user.service;


import com.google.common.base.Joiner;
import core.framework.database.JPAAccess;
import demo.user.domain.Role;
import demo.user.web.role.CreateRoleRequest;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * @author chi
 */
@Component
public class RoleService {
    @Inject
    JPAAccess repository;

    public Role get(String id) {
        return repository.get(Role.class, id);
    }

    @Transactional
    public Role create(CreateRoleRequest request) {
        Role role = new Role();
        role.id = UUID.randomUUID().toString();
        role.name = request.name;
        role.permissions = request.permissions == null ? null : Joiner.on(";").join(request.permissions);
        role.status = request.status;
        role.updatedTime = OffsetDateTime.now();
        role.updatedBy = request.requestBy;
        role.createdTime = OffsetDateTime.now();
        role.createdBy = request.requestBy;
        repository.save(role);
        return role;
    }
}
