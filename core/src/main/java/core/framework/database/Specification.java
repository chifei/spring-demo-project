package core.framework.database;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author neo
 */
public interface Specification<T> {
    void build(CriteriaQuery<T> query, Root<T> root, CriteriaBuilder builder);
}
