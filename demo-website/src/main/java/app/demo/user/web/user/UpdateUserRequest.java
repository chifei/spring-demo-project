package app.demo.user.web.user;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateUserRequest {
    @NotNull
    @Size(max = 128)
    @XmlElement(name = "email")
    public String email;
    @XmlElement(name = "roleIds")
    public List<String> roleIds;
    @Size(max = 64)
    @XmlElement(name = "requestBy")
    public String requestBy;
}
