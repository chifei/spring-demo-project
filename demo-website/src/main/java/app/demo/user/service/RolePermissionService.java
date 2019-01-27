package app.demo.user.service;


import app.demo.common.database.JpaRepository;
import app.demo.common.database.Query;
import app.demo.user.domain.RolePermission;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;


@Component
public class RolePermissionService {
    @Inject
    JpaRepository repository;

    @Transactional
    public RolePermission create(String roleId, String permissionName) {
        RolePermission rolePermission = new RolePermission();
        rolePermission.id = UUID.randomUUID().toString();
        rolePermission.permissionName = permissionName;
        rolePermission.roleId = roleId;
        repository.save(rolePermission);
        return rolePermission;
    }

    public List<String> getPermissionNames(String roleId) {
        Query query = Query.create("SELECT t.permissionName FROM RolePermission t WHERE 1=1");
        query.append("AND t.roleId=:roleId");
        query.param("roleId", roleId);
        return repository.find(query);
    }

    @Transactional
    public void deleteByRoleId(String roleId) {
        Query query = Query.create("DELETE FROM RolePermission t WHERE 1=1");
        query.append("AND t.roleId=:roleId");
        query.param("roleId", roleId);
        repository.update(query);
    }
}
