package demo.user.web.role;

import demo.user.domain.RoleStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RoleResponse {
    @XmlElement(name = "id")
    public String id;

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "permissions")
    public List<String> permissions;

    @XmlElement(name = "status")
    public RoleStatus status;

    @XmlElement(name = "requestBy")
    public String requestBy;
}
