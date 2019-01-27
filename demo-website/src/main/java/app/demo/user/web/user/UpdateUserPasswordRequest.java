package app.demo.user.web.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateUserPasswordRequest {
    @NotNull
    @Size(max = 32)
    @XmlElement(name = "password")
    public String password;
    @Size(max = 64)
    @XmlElement(name = "requestBy")
    public String requestBy;
}
