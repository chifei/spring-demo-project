package core.framework.monitor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author neo
 */
@XmlRootElement(name = "server_status")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerStatus {
    @XmlElement(name = "disabled")
    public boolean disabled;
}
