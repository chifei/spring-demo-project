package app.demo.common.database;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.SharedEntityManagerCreator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class JpaRepository {
    private final Logger logger = LoggerFactory.getLogger(JpaRepository.class);
    private EntityManager entityManager;

    public <T> T get(Class<T> entityClass, Object id) {
        Stopwatch watch = Stopwatch.createStarted();
        try {
            return entityManager.find(entityClass, id);
        } finally {
            logger.debug("get, entityClass={}, id={}, elapsedTime={}", entityClass.getName(), id, watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public void save(Object entity) {
        Stopwatch watch = Stopwatch.createStarted();
        try {
            entityManager.persist(entity);
        } finally {
            logger.debug("save, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }


    public void delete(Object entity) {
        Stopwatch watch = Stopwatch.createStarted();
        try {
            entityManager.merge(entity);
            entityManager.remove(entity);
        } finally {
            logger.debug("delete, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public void refresh(Object entity) {
        Stopwatch watch = Stopwatch.createStarted();
        try {
            entityManager.refresh(entity);
        } finally {
            logger.debug("refresh, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public void detach(Object entity) {
        Stopwatch watch = Stopwatch.createStarted();
        try {
            entityManager.detach(entity);
        } finally {
            logger.debug("detach, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public <T> List<T> find(CriteriaQuery<T> query) {
        Stopwatch watch = Stopwatch.createStarted();
        try {
            return entityManager.createQuery(query).getResultList();
        } finally {
            logger.debug("find by CriteriaQuery<T>, elapsedTime={}", watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> find(Query query) {
        Stopwatch watch = Stopwatch.createStarted();
        try {
            return query.query(entityManager).getResultList();
        } finally {
            logger.debug("find, query={}, params={}, from={}, size={}, elapsedTime={}",
                query.queryString, query.params, query.from, query.size, watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public <T> T findOne(CriteriaQuery<T> query) {
        Stopwatch watch = Stopwatch.createStarted();
        try {
            List<T> results = entityManager.createQuery(query).getResultList();
            return getOne(results);
        } finally {
            logger.debug("findOne by CriteriaQuery<T>, elapsedTime={}", watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T findOne(Query query) {
        Stopwatch watch = Stopwatch.createStarted();
        try {
            List<T> results = query.query(entityManager).getResultList();
            return getOne(results);
        } finally {
            logger.debug("findOne, query={}, params={}, elapsedTime={}", query.queryString, query.params, watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    @SuppressWarnings("unchecked")
    public long count(Query query) {
        Stopwatch watch = Stopwatch.createStarted();
        try {
            return (Long) query.query(entityManager).getSingleResult();
        } finally {
            logger.debug("findOne, query={}, params={}, elapsedTime={}", query.queryString, query.params, watch.elapsed(TimeUnit.MILLISECONDS));
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
        Stopwatch watch = Stopwatch.createStarted();
        try {
            return query.query(entityManager).executeUpdate();
        } finally {
            logger.debug("update, query={}, params={}, elapsedTime={}", query.queryString, query.params, watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public void update(Object entity) {
        Stopwatch watch = Stopwatch.createStarted();
        try {
            entityManager.merge(entity);
        } finally {
            logger.debug("update, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public void flush() {
        Stopwatch watch = Stopwatch.createStarted();
        try {
            entityManager.flush();
        } finally {
            logger.debug("flush, elapsedTime={}", watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public CriteriaBuilder criteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        entityManager = SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
    }
}
