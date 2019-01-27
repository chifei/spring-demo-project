package app.demo.user.web.user;


import app.demo.user.domain.UserStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class CreateUserRequest {
    @NotNull
    @Size(max = 36)
    @XmlElement(name = "username")
    public String username;
    @NotNull
    @Size(max = 32)
    @XmlElement(name = "password")
    public String password;
    @NotNull
    @Size(max = 128)
    @XmlElement(name = "email")
    public String email;
    @XmlElement(name = "roleIds")
    public List<String> roleIds;
    @XmlElement(name = "status")
    public UserStatus status;
    @Size(max = 64)
    @XmlElement(name = "requestBy")
    public String requestBy;
}
