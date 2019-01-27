package app.demo.user.web.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class LoginResponse {
    @XmlElement(name = "success")
    public Boolean success;
    @XmlElement(name = "message")
    public String message;
    @XmlElement(name = "fromURL")
    public String fromURL;
}
