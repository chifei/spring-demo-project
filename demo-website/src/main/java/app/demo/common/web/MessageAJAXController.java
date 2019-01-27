package app.demo.common.web;


import app.demo.common.util.Messages;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;


@RestController
public class MessageAJAXController {
    @Inject
    Messages messages;

    @RequestMapping(value = "/admin/api/messages/{language}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Map<String, String> index(@PathVariable("language") String language) {
        Locale locale = Locale.forLanguageTag(language);
        return this.messages.getMessages(locale);
    }
}
