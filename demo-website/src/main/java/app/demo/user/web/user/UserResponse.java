package app.demo.user.web.user;

import app.demo.user.domain.UserStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class UserResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "email")
    public String email;
    @XmlElement(name = "roleIds")
    public List<String> roleIds;
    @XmlElement(name = "roleNames")
    public List<String> roleNames;
    @XmlElement(name = "status")
    public UserStatus status;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
}
