package demo.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author neo
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class HomeForm {
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "price")
    double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
