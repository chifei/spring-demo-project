package core.test.rest.web;

import core.framework.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author neo
 */
@RestController
public class ExceptionHandlingTestController {
    @RequestMapping(value = "/test/method-not-allowed", produces = "application/xml", method = RequestMethod.POST)
    public String onlyAllowPOST() {
        return "<result/>";
    }

    @RequestMapping(value = "/test/media-type-not-supported", consumes = "application/xml", produces = "application/xml")
    public String mediaTypeNotSupported(String request) {
        return "<result/>";
    }

    @RequestMapping(value = "/test/invalid-request")
    public String invalidRequest(@Valid @RequestBody TestRequest request) {
        return "result";
    }

    @RequestMapping(value = "/test/not-found")
    public String notFound() {
        throw new ResourceNotFoundException("not found");
    }

    @XmlRootElement(name = "request")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestRequest {
        @NotNull
        @XmlElement(name = "field")
        public String field;
    }
}
