package app.demo.user.web.role;


import app.demo.user.domain.RoleStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateRoleRequest {
    @NotNull
    @Size(max = 36)
    @XmlElement(name = "name")
    public String name;
    @NotNull
    @XmlElement(name = "permissions")
    public List<String> permissions;
    @XmlElement(name = "status")
    public RoleStatus status;
    @Size(max = 64)
    @XmlElement(name = "requestBy")
    public String requestBy;
}
