package core.framework.web.rest.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author neo
 */
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorResponse {
    @XmlElement(name = "message")
    public String message;
    @XmlElement(name = "exception_trace")
    public String exceptionTrace;
    @XmlElement(name = "exception_class")
    public String exceptionClass;
}
