package core.framework.mail;

import com.google.common.base.Charsets;
import core.framework.task.TaskExecutor;
import core.framework.util.StringUtils;
import core.framework.util.TimeLength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author neo
 */
public class MailSender {
    private final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private final JavaMailSenderImpl sender = new JavaMailSenderImpl();
    @Inject
    TaskExecutor taskExecutor;

    public MailSender() {
        sender.setDefaultEncoding(Charsets.UTF_8.name());
    }

    public void send(Mail mail) {
        try {
            MimeMessage message = createMimeMessage(mail);
            logger.debug("start sending email");
            sender.send(message);
        } catch (MessagingException e) {
            throw new MailException(e);
        } finally {
            logger.debug("finish sending email");
        }
    }

    public void sendAsync(final Mail mail) {
        taskExecutor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                send(mail);
                return null;
            }
        });
    }

    private MimeMessage createMimeMessage(Mail mail) throws MessagingException {
        MimeMessageHelper message = new MimeMessageHelper(sender.createMimeMessage());
        logger.debug("subject={}", mail.subject());
        message.setSubject(mail.subject());
        logger.debug("from={}", mail.from());
        message.setFrom(mail.from());
        logger.debug("to={}", mail.toAddresses());
        message.setTo(toAddressArray(mail.toAddresses()));
        logger.debug("cc={}", mail.ccAddresses());
        message.setCc(toAddressArray(mail.ccAddresses()));
        logger.debug("bcc={}", mail.bccAddresses());
        message.setBcc(toAddressArray(mail.bccAddresses()));
        message.setText(mail.body(), Mail.CONTENT_TYPE_HTML.equals(mail.contentType()));
        setReplyTo(mail, message);
        return message.getMimeMessage();
    }

    void setReplyTo(Mail mail, MimeMessageHelper message) throws MessagingException {
        if (StringUtils.hasText(mail.replyTo())) {
            message.setReplyTo(mail.replyTo());
        } else {
            message.setReplyTo(mail.from());
        }
    }

    private String[] toAddressArray(List<String> addresses) {
        return addresses.toArray(new String[addresses.size()]);
    }

    public void setHost(String host) {
        if (StringUtils.hasText(host))
            sender.setHost(host);
    }

    public void setPort(Integer port) {
        if (port != null)
            sender.setPort(port);
    }

    public void setUsername(String username) {
        if (StringUtils.hasText(username)) {
            sender.setUsername(username);
            sender.getJavaMailProperties().put("mail.smtp.auth", "true");
        }
    }

    public void setPassword(String password) {
        if (StringUtils.hasText(password))
            sender.setPassword(password);
    }

    public void setTimeout(TimeLength timeout) {
        if (timeout != null)
            sender.getJavaMailProperties().put("mail.smtp.timeout", timeout.toMilliseconds());
    }
}
