package core.framework.mail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author neo
 */
public class MailSenderTest {
    MailSender mailSender;
    MimeMessageHelper message;

    @Before
    public void createMailSender() {
        mailSender = new MailSender();
        message = mock(MimeMessageHelper.class);
    }

    @Test
    public void useFromAddressAsReplyTo() throws MessagingException {
        Mail mail = new Mail();
        mail.from("from@mail.com");
        mailSender.setReplyTo(mail, message);

        verify(message).setReplyTo("from@mail.com");
    }

    @Test
    public void useReplyToAddress() throws MessagingException {
        Mail mail = new Mail();
        mail.from("from@mail.com");
        mail.replyTo("replyTo@mail.com");
        mailSender.setReplyTo(mail, message);

        verify(message).setReplyTo("replyTo@mail.com");
    }
}
