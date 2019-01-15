package core.framework.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author neo
 */
public class Mail {
    static final String CONTENT_TYPE_HTML = "text/html";
    private final List<String> toAddresses = new ArrayList<>();
    private final List<String> ccAddresses = new ArrayList<>();
    private final List<String> bccAddresses = new ArrayList<>();
    private String contentType;
    private String replyTo;
    private String subject;
    private String from;
    private String body;

    public Mail addTo(String toAddress) {
        toAddresses.add(toAddress);
        return this;
    }

    public Mail addCC(String ccAddress) {
        ccAddresses.add(ccAddress);
        return this;
    }

    public Mail addBCC(String bccAddress) {
        bccAddresses.add(bccAddress);
        return this;
    }

    public List<String> toAddresses() {
        return toAddresses;
    }

    public List<String> ccAddresses() {
        return ccAddresses;
    }

    public List<String> bccAddresses() {
        return bccAddresses;
    }

    public Mail htmlBody(String body) {
        this.body = body;
        contentType = CONTENT_TYPE_HTML;
        return this;
    }

    public Mail plainBody(String body) {
        this.body = body;
        return this;
    }

    public String replyTo() {
        return replyTo;
    }

    public String subject() {
        return subject;
    }

    public String from() {
        return from;
    }

    public String body() {
        return body;
    }

    public String contentType() {
        return contentType;
    }

    public Mail replyTo(String replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public Mail subject(String subject) {
        this.subject = subject;
        return this;
    }

    public Mail from(String from) {
        this.from = from;
        return this;
    }
}
