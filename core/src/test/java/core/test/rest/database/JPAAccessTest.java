package core.test.rest.database;

import core.framework.database.JPAAccess;
import core.framework.database.Query;
import core.test.rest.SpringTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author neo
 */
public class JPAAccessTest extends SpringTest {
    @Inject
    JPAAccess jpaAccess;

    @Test
    @Transactional
    public void save() {
        JPATestEntity entity = createEntity("someName");
        Assert.assertNotNull(entity.getId());
    }

    @Test
    @Transactional
    public void get() {
        JPATestEntity entity = createEntity("someName");
        JPATestEntity loadedEntity = jpaAccess.get(JPATestEntity.class, entity.getId());
        Assert.assertEquals("someName", loadedEntity.getName());
    }

    @Test
    @Transactional
    public void update() {
        JPATestEntity entity = createEntity("someName");
        entity.setName("someOtherName");
        jpaAccess.update(entity);
        JPATestEntity loadedEntity = jpaAccess.get(JPATestEntity.class, entity.getId());
        Assert.assertEquals("someOtherName", loadedEntity.getName());
    }

    @Test
    @Transactional
    public void find() {
        createEntity("someName");

        List<JPATestEntity> results = jpaAccess.find(Query.create("from JPATestEntity where name = :name")
            .param("name", "someName"));
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("someName", results.get(0).getName());
    }

    @Test
    @Transactional
    public void findWithOffset() {
        createEntity("someName");
        createEntity("someName");

        List<JPATestEntity> results = jpaAccess.find(Query.create("from JPATestEntity where name = :name")
            .param("name", "someName")
            .from(0).fetch(2));
        Assert.assertEquals(2, results.size());

        results = jpaAccess.find(Query.create("from JPATestEntity where name = :name")
            .param("name", "someName")
            .from(1).fetch(1));
        Assert.assertEquals(1, results.size());
    }

    @Test
    @Transactional
    public void findByCriteria() {
        createEntity("someName");

        CriteriaBuilder builder = jpaAccess.criteriaBuilder();
        CriteriaQuery<JPATestEntity> query = builder.createQuery(JPATestEntity.class);
        Root<JPATestEntity> root = query.from(JPATestEntity.class);
        query.where(builder.equal(root.get("name"), "someName"));
        List<JPATestEntity> results = jpaAccess.find(query);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("someName", results.get(0).getName());
    }

    @Test
    @Transactional
    public void findOneReturnsNull() {
        JPATestEntity result = jpaAccess.findOne(Query.create("from JPATestEntity"));
        Assert.assertNull(result);
    }

    @Test
    @Transactional
    public void updateByQuery() {
        JPATestEntity entity = createEntity("someName");
        jpaAccess.update(Query.create("update JPATestEntity t SET t.name = :name where t.id = :id").param("id", entity.getId()).param("name", "newName"));

        JPATestEntity loadedEntity = jpaAccess.get(JPATestEntity.class, entity.getId());
        Assert.assertEquals("newName", loadedEntity.getName());
    }

    private JPATestEntity createEntity(String name) {
        JPATestEntity entity = new JPATestEntity();
        entity.setName(name);
        jpaAccess.save(entity);
        jpaAccess.flush();
        jpaAccess.detach(entity);
        return entity;
    }
}
