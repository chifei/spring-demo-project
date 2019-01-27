package app.demo.user.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "role_permission")
public class RolePermission {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "role_id", length = 36)
    public String roleId;

    @Column(name = "permission_name", length = 64)
    public String permissionName;

}
