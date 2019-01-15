package core.test.rest.database;

import core.test.rest.SpringTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * @author neo
 */
@Transactional
public class JPARepositoryTest extends SpringTest {
    @Inject
    JPATestEntityRepository repository;

    @Test
    public void findByName() {
        createEntity("someName");

        JPATestEntity entity = repository.findByName("someName");

        Assert.assertNotNull(entity);
    }

    private JPATestEntity createEntity(String name) {
        JPATestEntity entity = new JPATestEntity();
        entity.setName(name);
        repository.save(entity);
        return entity;
    }
}
