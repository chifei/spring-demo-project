package app.demo.user.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;


@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "name", length = 64)
    public String name;

    @Column(name = "display_name", length = 64)
    public String displayName;

    @Column(name = "description", length = 512)
    public String description;

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public PermissionStatus status;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
