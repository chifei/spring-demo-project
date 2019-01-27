package app.demo.user.web.permission;


import app.demo.user.domain.PermissionStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class CreatePermissionRequest {
    @NotNull
    @Size(max = 64)
    @XmlElement(name = "name")
    public String name;
    @NotNull
    @Size(max = 64)
    @XmlElement(name = "displayName")
    public String displayName;
    @Size(max = 512)
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "status")
    public PermissionStatus status;
    @Size(max = 64)
    @XmlElement(name = "requestBy")
    public String requestBy;
}
