package app.demo.product.web.product;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateProductRequest {
    @NotNull
    @Size(max = 128)
    @XmlElement(name = "name")
    public String name;
    @Size(max = 512)
    @XmlElement(name = "description")
    public String description;
    @Size(max = 64)
    @XmlElement(name = "requestBy")
    public String requestBy;
}
