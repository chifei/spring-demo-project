package app.demo.user.web.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class UserQuery {
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "roleId")
    public String roleId;
    @XmlElement(name = "page")
    public Integer page = 1;
    @XmlElement(name = "limit")
    public Integer limit = 20;
}
