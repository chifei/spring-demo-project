package demo.product.web.product;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductQuery {
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "page")
    public Integer page = 1;
    @XmlElement(name = "limit")
    public Integer limit = 20;
}
