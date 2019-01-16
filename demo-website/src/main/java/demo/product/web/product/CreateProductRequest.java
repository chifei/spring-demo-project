package demo.product.web.product;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateProductRequest {
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "description")
    public String description;
}
