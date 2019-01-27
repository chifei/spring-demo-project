package app.demo.user.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;


@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "username", length = 64)
    public String username;

    @Column(name = "email", length = 128)
    public String email;

    @Column(name = "salt", length = 64)
    public String salt;

    @Column(name = "iteration")
    public Integer iteration;

    @Column(name = "hashed_password", length = 64)
    public String hashedPassword;

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public UserStatus status;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
