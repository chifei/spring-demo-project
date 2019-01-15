package demo.user.web;


import core.framework.web.i18n.Messages;
import demo.user.domain.User;
import demo.user.service.UserService;
import demo.user.web.user.LoginRequest;
import demo.user.web.user.LoginResponse;
import demo.web.UserInfo;
import demo.web.UserPreference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chi
 */
@RestController
public class UserAJAXController {
    private final Logger logger = LoggerFactory.getLogger(UserAJAXController.class);

    @Inject
    Messages messages;
    @Inject
    UserService userService;
    @Inject
    UserInfo userInfo;
    @Inject
    UserPreference userPreference;

    @RequestMapping(value = "/admin/api/user/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        logger.info("response" + response);

        User user = userService.login(loginRequest);
        LoginResponse loginResponse = new LoginResponse();
        if (user == null) {
            loginResponse.success = false;
            loginResponse.message = messages.getMessage("user.loginError", userPreference.locale());
        } else {
            loginResponse.success = true;
            userInfo.setUserId(user.id);
            loginResponse.fromURL = "/";
            response.addCookie(new Cookie("test", "value"));
        }
        return loginResponse;
    }
}
