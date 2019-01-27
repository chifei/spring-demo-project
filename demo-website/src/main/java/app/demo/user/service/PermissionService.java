package app.demo.user.service;

import app.demo.common.database.JpaRepository;
import app.demo.common.database.Query;
import app.demo.user.domain.Permission;
import app.demo.user.web.permission.CreatePermissionRequest;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@Component
public class PermissionService {
    @Inject
    JpaRepository repository;

    @Transactional
    public Permission create(CreatePermissionRequest request) {
        Permission permission = new Permission();
        permission.id = UUID.randomUUID().toString();
        permission.name = request.name;
        permission.displayName = request.displayName;
        permission.description = request.description;
        permission.status = request.status;
        permission.updatedTime = OffsetDateTime.now();
        permission.updatedBy = request.requestBy;
        permission.createdTime = OffsetDateTime.now();
        permission.createdBy = request.requestBy;
        repository.save(permission);
        return permission;
    }

    public List<Permission> permissions() {
        Query query = Query.create("SELECT t FROM Permission t WHERE 1=1");
        return repository.find(query);
    }

}
