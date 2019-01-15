package demo.user.web.role;


import demo.user.domain.RoleStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateRoleRequest {
    @NotNull
    @Size(max = 36)
    @XmlElement(name = "name")
    public String name;
    @NotNull
    @XmlElement(name = "permissions")
    public List<String> permissions;
    @NotNull
    @XmlElement(name = "status")
    public RoleStatus status;
    @NotNull
    @Size(max = 63)
    @XmlElement(name = "requestBy")
    public String requestBy;
}
