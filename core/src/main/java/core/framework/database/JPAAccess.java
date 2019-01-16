package core.framework.database;

import core.framework.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.SharedEntityManagerCreator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * @author neo
 */
public class JPAAccess {
    private final Logger logger = LoggerFactory.getLogger(JPAAccess.class);

    private EntityManager entityManager;

    public <T> T get(Class<T> entityClass, Object id) {
        StopWatch watch = new StopWatch();
        try {
            return entityManager.find(entityClass, id);
        } finally {
            logger.debug("get, entityClass={}, id={}, elapsedTime={}", entityClass.getName(), id, watch.elapsedTime());
        }
    }

    public void save(Object entity) {
        StopWatch watch = new StopWatch();
        try {
            entityManager.persist(entity);
        } finally {
            logger.debug("save, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsedTime());
        }
    }

    public void update(Object entity) {
        StopWatch watch = new StopWatch();
        try {
            entityManager.merge(entity);
        } finally {
            logger.debug("update, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsedTime());
        }
    }

    public void delete(Object entity) {
        StopWatch watch = new StopWatch();
        try {
            entityManager.merge(entity);
            entityManager.remove(entity);
        } finally {
            logger.debug("delete, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsedTime());
        }
    }

    public void refresh(Object entity) {
        StopWatch watch = new StopWatch();
        try {
            entityManager.refresh(entity);
        } finally {
            logger.debug("refresh, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsedTime());
        }
    }

    public void detach(Object entity) {
        StopWatch watch = new StopWatch();
        try {
            entityManager.detach(entity);
        } finally {
            logger.debug("detach, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsedTime());
        }
    }

    public <T> List<T> find(CriteriaQuery<T> query) {
        StopWatch watch = new StopWatch();
        try {
            return entityManager.createQuery(query).getResultList();
        } finally {
            logger.debug("find by CriteriaQuery<T>, elapsedTime={}", watch.elapsedTime());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> find(Query query) {
        StopWatch watch = new StopWatch();
        try {
            return query.query(entityManager).getResultList();
        } finally {
            logger.debug("find, query={}, params={}, from={}, size={}, elapsedTime={}",
                query.queryString, query.params, query.from, query.size, watch.elapsedTime());
        }
    }

    public <T> T findOne(CriteriaQuery<T> query) {
        StopWatch watch = new StopWatch();
        try {
            List<T> results = entityManager.createQuery(query).getResultList();
            return getOne(results);
        } finally {
            logger.debug("findOne by CriteriaQuery<T>, elapsedTime={}", watch.elapsedTime());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T findOne(Query query) {
        StopWatch watch = new StopWatch();
        try {
            List<T> results = query.query(entityManager).getResultList();
            return getOne(results);
        } finally {
            logger.debug("findOne, query={}, params={}, elapsedTime={}", query.queryString, query.params, watch.elapsedTime());
        }
    }

    @SuppressWarnings("unchecked")
    public long count(Query query) {
        StopWatch watch = new StopWatch();
        try {
            return (Long) query.query(entityManager).getSingleResult();
        } finally {
            logger.debug("findOne, query={}, params={}, elapsedTime={}", query.queryString, query.params, watch.elapsedTime());
        }
    }

    private <T> T getOne(List<T> results) {
        if (results.isEmpty()) return null;
        if (results.size() > 1) {
            throw new NonUniqueResultException("result returned more than one element, returnedSize=" + results.size());
        }
        return results.get(0);
    }

    public int update(Query query) {
        StopWatch watch = new StopWatch();
        try {
            return query.query(entityManager).executeUpdate();
        } finally {
            logger.debug("update, query={}, params={}, elapsedTime={}", query.queryString, query.params, watch.elapsedTime());
        }
    }

    public void flush() {
        StopWatch watch = new StopWatch();
        try {
            entityManager.flush();
        } finally {
            logger.debug("flush, elapsedTime={}", watch.elapsedTime());
        }
    }

    public CriteriaBuilder criteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        entityManager = SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
    }
}
