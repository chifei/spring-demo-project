package app.demo.product.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;


@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "name", length = 128)
    public String name;

    @Column(name = "description", length = 512)
    public String description;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
