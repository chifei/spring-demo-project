package app.demo.user.web.role;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteRoleRequest {
    @XmlElement(name = "ids")
    public List<String> ids;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
