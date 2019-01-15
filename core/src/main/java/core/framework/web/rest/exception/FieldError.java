package core.framework.web.rest.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author neo
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldError {
    @XmlElement(name = "field")
    public String field;
    @XmlElement(name = "message")
    public String message;
}
