package core.framework.managment.web;

import core.framework.monitor.MonitorAccessControl;
import core.framework.monitor.ServerStatus;
import core.framework.web.request.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author neo
 */
@RestController
public class ServerStatusManagementController {
    private final Logger logger = LoggerFactory.getLogger(ServerStatusManagementController.class);
    @Inject
    RequestContext requestContext;
    @Inject
    ServerStatus serverStatus;

    @RequestMapping(value = "/management/server-status", method = RequestMethod.PUT)
    @ResponseBody
    public ServerStatus update(@RequestParam(value = "disable", defaultValue = "false") boolean disable) {
        MonitorAccessControl.assertFromInternalNetwork(requestContext.clientIP());

        logger.info("updated server-status, disable={}, updatedBy={}", disable, requestContext.clientIP());
        serverStatus.disabled = disable;
        return serverStatus;
    }
}
