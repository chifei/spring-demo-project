package core.test.rest.database;

import core.framework.database.EntityRowMapper;
import core.framework.database.JDBCAccess;
import core.framework.util.DateUtils;
import core.test.rest.SpringTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.persistence.Column;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author neo
 */
public class EntityRowMapperTest extends SpringTest {
    @Inject
    JDBCAccess jdbcAccess;

    @Before
    public void createTestTable() {
        jdbcAccess.execute("create table entity_mapper_test (id int, field1 varchar(20), field2 datetime)");
        jdbcAccess.execute("insert into entity_mapper_test (id, field1, field2) values (?, ?, ?)",
            1, "value1", DateUtils.date(2012, Calendar.DECEMBER, 24));
        jdbcAccess.execute("insert into entity_mapper_test (id, field1, field2) values (?, ?, ?)",
            2, "value2", DateUtils.date(2012, Calendar.DECEMBER, 25));
    }

    @After
    public void dropTestTable() {
        jdbcAccess.execute("drop table entity_mapper_test");
    }

    @Test
    public void findBySQL() {
        List<TestEntity> entities = jdbcAccess.find("select * from entity_mapper_test order by id",
            EntityRowMapper.rowMapper(TestEntity.class));

        assertEquals(2, entities.size());
        assertEquals(1, entities.get(0).getId());
        assertEquals("value1", entities.get(0).getStringField());
        assertEquals(DateUtils.date(2012, Calendar.DECEMBER, 24), entities.get(0).getDateField());

        assertEquals(2, entities.get(1).getId());
        assertEquals("value2", entities.get(1).getStringField());
        assertEquals(DateUtils.date(2012, Calendar.DECEMBER, 25), entities.get(1).getDateField());
    }

    @Test
    public void findUniqueResultBySQL() {
        TestEntity entity = jdbcAccess.findOne("select * from entity_mapper_test where id = ?",
            EntityRowMapper.rowMapper(TestEntity.class), 1);

        assertEquals(1, entity.getId());
        assertEquals("value1", entity.getStringField());
        assertEquals(DateUtils.date(2012, Calendar.DECEMBER, 24), entity.getDateField());
    }

    public static class TestEntity {
        @Column(name = "id")
        private int id;
        @Column(name = "field1")
        private String stringField;
        @Column(name = "field2")
        private Date dateField;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public Date getDateField() {
            return dateField;
        }

        public void setDateField(Date dateField) {
            this.dateField = dateField;
        }
    }
}
