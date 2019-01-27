package app.demo.common.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class JSONTestBean {
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    public List<JSONTestBeanItem> items = new ArrayList<>();
    @XmlElementWrapper(name = "string-items")
    @XmlElement(name = "string-item")
    public List<String> stringItems = new ArrayList<>();
    @XmlElement(name = "different-field")
    public String field;
    @XmlElement(name = "date")
    public Date date;
    @XmlElement(name = "offsetDateTime")
    public OffsetDateTime offsetDateTime;
}
