package app.demo.user.service;


import app.demo.common.database.JpaRepository;
import app.demo.common.database.Query;
import app.demo.user.domain.Role;
import app.demo.user.web.role.CreateRoleRequest;
import app.demo.user.web.role.RoleQuery;
import app.demo.user.web.role.UpdateRoleRequest;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@Component
public class RoleService {
    @Inject
    JpaRepository repository;

    @Inject
    RolePermissionService rolePermissionService;

    @Inject
    EntityManagerFactory emf;

    public Role get(String id) {
        return repository.get(Role.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Role> find(RoleQuery roleQuery) {
        StringBuilder sql = new StringBuilder(64).append("SELECT * FROM role WHERE 1=1");
        if (roleQuery.name != null) {
            sql.append("AND name like ?");
        }
        sql.append(" limit ?, ?");
        EntityManager em = emf.createEntityManager();
        try {
            javax.persistence.Query query = em.createNativeQuery(sql.toString(), Role.class);
            int index = 1;
            if (roleQuery.name != null) {
                query.setParameter(index++, '%' + roleQuery.name + '%');
            }
            query.setParameter(index++, (roleQuery.page - 1) * roleQuery.limit);
            query.setParameter(index, roleQuery.limit);
            return (List<Role>) query.getResultList();
        } finally {
            em.close();
        }
    }

    public long count(RoleQuery roleQuery) {
        Query query = Query.create("SELECT count(t) FROM Role t WHERE 1=1");
        if (roleQuery.name != null) {
            query.append("AND t.name like :name");
            query.param("name", '%' + roleQuery.name + '%');
        }
        return repository.count(query);
    }

    @Transactional
    public Role create(CreateRoleRequest request) {
        Role role = new Role();
        role.id = UUID.randomUUID().toString();
        role.name = request.name;
        role.status = request.status;
        role.updatedTime = OffsetDateTime.now();
        role.updatedBy = request.requestBy;
        role.createdTime = OffsetDateTime.now();
        role.createdBy = request.requestBy;
        repository.save(role);
        if (request.permissions != null && !request.permissions.isEmpty()) {
            for (String permission : request.permissions) {
                rolePermissionService.create(role.id, permission);
            }
        }
        return role;
    }

    @Transactional
    public Role update(String id, UpdateRoleRequest request) {
        Role role = get(id);
        role.name = request.name;
        role.status = request.status;
        role.updatedTime = OffsetDateTime.now();
        role.updatedBy = request.requestBy;
        repository.update(role);
        if (request.permissions != null && !request.permissions.isEmpty()) {
            rolePermissionService.deleteByRoleId(role.id);
            for (String permission : request.permissions) {
                rolePermissionService.create(role.id, permission);
            }
        }
        return role;
    }

    @Transactional
    public void batchDelete(List<String> ids) {
        StringBuilder b = new StringBuilder(64);
        b.append("DELETE FROM Role t WHERE t.id in (");
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
        repository.update(Query.create(b.toString()));
    }
}
