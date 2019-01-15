package demo.product.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * @author neo
 */
@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "id")
    public String id;

    @Column(name = "name", length = 50)
    public String name;

    @Column(name = "updated_date")
    public Date updatedDate;
}
