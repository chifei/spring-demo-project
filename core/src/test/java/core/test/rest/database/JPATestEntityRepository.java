package core.test.rest.database;

import core.framework.database.JPARepository;
import org.springframework.stereotype.Repository;

/**
 * @author neo
 */
@Repository
public class JPATestEntityRepository extends JPARepository<JPATestEntity> {
    public JPATestEntity findByName(final String name) {
        return findOne((query, root, builder) -> query.where(builder.equal(root.get("name"), name)));
    }
}
