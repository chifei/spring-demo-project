package demo.customer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * @author neo
 */
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @Column(name = "id")
    @GeneratedValue
    public Long id;

    @Column(name = "name", length = 50)
    public String name;

    @Column(name = "email", length = 100)
    public String email;

    @Column(name = "updated_date")
    public Date updatedDate;
}
