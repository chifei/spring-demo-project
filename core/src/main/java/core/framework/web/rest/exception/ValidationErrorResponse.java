package core.framework.web.rest.exception;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author neo
 */
@XmlRootElement(name = "validation_error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidationErrorResponse {
    @XmlElementWrapper(name = "field_errors")
    @XmlElement(name = "field_error")
    public final List<FieldError> fieldErrors = Lists.newArrayList();
}
