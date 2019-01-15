package core.framework.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author neo
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class JSONTestBean {
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private final List<JSONTestBeanItem> items = new ArrayList<>();
    @XmlElementWrapper(name = "string-items")
    @XmlElement(name = "string-item")
    private final List<String> stringItems = new ArrayList<>();
    @XmlElement(name = "different-field")
    private String field;
    @XmlElement(name = "date")
    private Date date;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<JSONTestBeanItem> getItems() {
        return items;
    }

    public List<String> getStringItems() {
        return stringItems;
    }
}
