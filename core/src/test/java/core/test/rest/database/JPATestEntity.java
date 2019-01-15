package core.test.rest.database;

import core.framework.database.URLSafeUUIDIdGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author neo
 */
@Entity
@GenericGenerator(name = "uuid", strategy = URLSafeUUIDIdGenerator.CLASS_NAME)
public class JPATestEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "name")
    private String name;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        JPATestEntity that = (JPATestEntity) object;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
