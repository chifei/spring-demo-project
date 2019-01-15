package demo.product.event;

import core.framework.event.Event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author neo
 */
@Event("product-created")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductCreatedEvent {
    @XmlElement(name = "product_id")
    public String productId;
    @XmlElement(name = "product_name")
    public String productName;
}
