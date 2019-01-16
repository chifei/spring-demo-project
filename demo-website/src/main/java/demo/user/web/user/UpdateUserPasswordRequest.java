package demo.user.web.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateUserPasswordRequest {
    @XmlElement(name = "password")
    public String password;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
