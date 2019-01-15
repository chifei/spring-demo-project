package core.framework.monitor.web;

import core.framework.monitor.ServerStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * with aws the health check url requires to expose to ELB which to outside, we don't want to expose /monitor/*
 * so to create safe health check url
 *
 * @author neo
 */
@RestController
public class HealthCheckController {
    @Inject
    ServerStatus serverStatus;

    @RequestMapping(value = "/health-check", method = {RequestMethod.HEAD, RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<Void> healthCheck() {
        if (serverStatus.disabled) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } else {
            return ResponseEntity.ok(null);
        }
    }
}
