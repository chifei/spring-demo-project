package app.demo.user.web.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteUserRequest {
    @XmlElement(name = "ids")
    public List<String> ids;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
