package demo.user.web.user;


import demo.user.domain.UserStatus;

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
public class CreateUserRequest {
    @NotNull
    @Size(max = 36)
    @XmlElement(name = "username")
    public String username;
    @NotNull
    @XmlElement(name = "password")
    public String password;
    @Size(max = 63)
    @XmlElement(name = "email")
    public String email;
    @Size(max = 11)
    @XmlElement(name = "userGroupIds")
    public List<String> roleIds;
    @NotNull
    @XmlElement(name = "status")
    public UserStatus status;
    @NotNull
    @Size(max = 63)
    @XmlElement(name = "requestBy")
    public String requestBy;
}
