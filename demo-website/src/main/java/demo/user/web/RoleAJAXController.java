package demo.user.web;


import core.framework.web.i18n.Messages;
import demo.user.service.RoleService;
import demo.user.web.role.RoleQueryResponse;
import demo.web.UserInfo;
import demo.web.UserPreference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author chi
 */
@RestController
public class RoleAJAXController {
    @Inject
    Messages messages;
    @Inject
    RoleService roleService;
    @Inject
    UserInfo userInfo;
    @Inject
    UserPreference userPreference;

    @RequestMapping(value = "/admin/api/user/role/find", method = RequestMethod.PUT)
    public RoleQueryResponse find() {
        RoleQueryResponse queryResponse = new RoleQueryResponse();
        queryResponse.items = roleService.find();
        queryResponse.total = roleService.count();
        return queryResponse;
    }
}
