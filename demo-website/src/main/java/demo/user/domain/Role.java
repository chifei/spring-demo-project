package demo.user.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "user_role")
public class Role {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "name", length = 64)
    public String name;

    @Column(name = "permissions", length = 512)
    public String permissions;

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public RoleStatus status;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
