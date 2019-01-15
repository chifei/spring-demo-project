package core.framework.mail;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author neo
 */
public class MailTest {
    @Test
    public void setHTMLContent() {
        Mail mail = new Mail();
        mail.htmlBody("<html/>");

        Assert.assertEquals(Mail.CONTENT_TYPE_HTML, mail.contentType());
    }
}
