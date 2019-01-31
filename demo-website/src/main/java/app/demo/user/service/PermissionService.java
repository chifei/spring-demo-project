package app.demo.user.service;

import app.demo.common.database.JpaRepository;
import app.demo.user.domain.Permission;
import app.demo.user.web.permission.CreatePermissionRequest;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@Component
public class PermissionService {
    @Inject
    JpaRepository repository;

    @Inject
    EntityManagerFactory emf;

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
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Permission> query = em.createQuery("SELECT t FROM Permission t", Permission.class);
            query.setHint("org.hibernate.cacheable", true);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

}
