package demo.user.service;


import com.google.common.base.Joiner;
import core.framework.database.JPAAccess;
import core.framework.database.Query;
import demo.user.domain.Role;
import demo.user.web.role.CreateRoleRequest;
import demo.user.web.role.RoleQuery;
import demo.user.web.role.UpdateRoleRequest;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
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

    public List<Role> find(RoleQuery roleQuery) {
        Query query = Query.create("SELECT t FROM Role t WHERE 1=1");
        if (roleQuery.name != null) {
            query.append("AND t.name=:name");
            query.param("name", roleQuery.name);
        }
        query.fetch(roleQuery.limit);
        query.from(roleQuery.limit * (roleQuery.page - 1));
        return repository.find(query);
    }

    public long count(RoleQuery roleQuery) {
        Query query = Query.create("SELECT count(t) FROM Role t WHERE 1=1");
        if (roleQuery.name != null) {
            query.append("AND t.name=:name");
            query.param("name", roleQuery.name);
        }
        return repository.count(query);
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

    @Transactional
    public Role update(String id, UpdateRoleRequest request) {
        Role role = get(id);
        role.name = request.name;
        role.permissions = request.permissions == null ? null : Joiner.on(";").join(request.permissions);
        role.status = request.status;
        role.updatedTime = OffsetDateTime.now();
        role.updatedBy = request.requestBy;
        repository.update(role);
        return role;
    }

    @Transactional
    public void batchDelete(List<String> ids) {
        StringBuilder b = new StringBuilder();
        b.append("DELETE FROM Role t WHERE t.id in (");
        int MAX_GROUP_COUNT = 1000;
        for (int i = 0; i < ids.size(); i++) {
            int index = i % MAX_GROUP_COUNT;
            if (i != 0 && index == 0) {
                b.append(") OR t.id in (");
            } else if (index != 0) {
                b.append(',');
            }
            b.append("'").append(ids.get(i)).append("'");
        }
        b.append(')');
        repository.update(Query.create(b.toString()));
    }
}
