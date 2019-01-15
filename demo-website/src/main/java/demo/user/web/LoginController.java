package demo.user.web;


import core.framework.util.JSONBinder;
import core.framework.web.i18n.Messages;
import demo.web.MessageBundleBuilder;
import demo.web.UserPreference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author chi
 */
@Controller
public class LoginController {
    @Inject
    Messages messages;

    @Inject
    UserPreference userPreference;

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String login(ModelMap model) {
        Map<String, String> messages = this.messages.getMessages(userPreference.locale());
        model.put("messages", "window.messages=" + JSONBinder.toJSON(new MessageBundleBuilder(messages).build()));
        return "/login.html";
    }
}
