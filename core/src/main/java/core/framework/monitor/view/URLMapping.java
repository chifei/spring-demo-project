package core.framework.monitor.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author neo
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class URLMapping {
    @XmlAttribute(name = "path")
    public String path;
    @XmlAttribute(name = "class")
    public String controllerClass;
    @XmlAttribute(name = "method")
    public String controllerMethod;
}
