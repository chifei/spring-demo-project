package app.demo.common.database;


import app.demo.SpringTest;
import app.demo.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class JpaRepositoryTest extends SpringTest {

    @Inject
    JpaRepository repository;

    @Test
    public void findOneByQuery() {
        Query query = Query.create("SELECT t FROM User t WHERE 1=1 and t.username='admin'");
        User user = repository.findOne(query);
        Assertions.assertEquals(user.username, "admin");
    }

    @Test
    public void findOneByCriteriaQuery() {
        CriteriaBuilder criteriaBuilder = repository.criteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("username"), "admin"));
        User user = repository.findOne(criteriaQuery);
        Assertions.assertEquals(user.username, "admin");
    }

}
