package core.framework.database;

import core.framework.exception.ResourceNotFoundException;
import core.framework.util.AssertUtils;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

import static org.springframework.core.GenericTypeResolver.resolveTypeArguments;

/**
 * @author neo
 */
public abstract class JPARepository<T> {
    protected final Class<T> entityClass;
    @Inject
    protected JPAAccess jpaAccess;

    @SuppressWarnings("unchecked")
    public JPARepository() {
        Class<?>[] arguments = resolveTypeArguments(getClass(), JPARepository.class);
        AssertUtils.assertTrue(arguments != null && arguments.length == 1, "repository must extend with generic type like JPARepository<T>, class={}", getClass());
        entityClass = (Class<T>) arguments[0];
    }

    public T get(Object id) {
        T entity = jpaAccess.get(entityClass, id);
        if (entity == null) throw new ResourceNotFoundException("entity not found, id=" + id);
        return entity;
    }

    public void save(T entity) {
        jpaAccess.save(entity);
    }

    public void update(T entity) {
        jpaAccess.update(entity);
    }

    public void delete(T entity) {
        jpaAccess.delete(entity);
    }

    public List<T> findAll() {
        return jpaAccess.find(Query.create("from " + entityClass.getName()));
    }

    public T findOne(Specification<T> specification) {
        CriteriaQuery<T> query = criteriaQuery(specification);
        return jpaAccess.findOne(query);
    }

    public List<T> find(Specification<T> specification) {
        CriteriaQuery<T> query = criteriaQuery(specification);
        return jpaAccess.find(query);
    }

    private CriteriaQuery<T> criteriaQuery(Specification<T> specification) {
        CriteriaBuilder builder = jpaAccess.criteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        specification.build(query, query.from(entityClass), builder);
        return query;
    }
}
