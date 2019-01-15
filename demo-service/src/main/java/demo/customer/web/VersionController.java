package demo.customer.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author neo
 */
@RestController
public class VersionController {
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public String version() {
        return "v1.0";
    }
}
