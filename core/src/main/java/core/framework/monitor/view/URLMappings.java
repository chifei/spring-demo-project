package core.framework.monitor.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author neo
 */
@XmlRootElement(name = "url_mappings")
@XmlAccessorType(XmlAccessType.FIELD)
public class URLMappings {
    @XmlElementWrapper(name = "mappings")
    @XmlElement(name = "mapping")
    public final List<URLMapping> mappings = new ArrayList<>();
}
