package core.framework.http;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author neo
 */
public class HTTPRequest {
    private final Logger logger = LoggerFactory.getLogger(HTTPRequest.class);
    RequestBuilder builder;
    String uri;
    String body;

    public HTTPRequest(String uri, HTTPMethod method) {
        logger.debug("uri={}, method={}", uri, method);
        this.uri = uri;
        builder = RequestBuilder.create(method.name())
            .setUri(uri);
    }

    public HTTPRequest accept(ContentType contentType) {
        return header(HttpHeaders.ACCEPT, contentType.toString());
    }

    public HTTPRequest header(String name, String value) {
        logger.debug("[header] {}={}", name, value);
        builder.setHeader(name, value);
        return this;
    }

    public HTTPRequest addParam(String name, String value) {
        logger.debug("[param] {}={}", name, value);
        builder.addParameter(name, value);
        return this;
    }

    public HTTPRequest text(String value, ContentType contentType) {
        logger.debug("[entity] value={}, contentType={}", value, contentType);
        this.body = value;
        builder.setEntity(new StringEntity(value, contentType));
        return this;
    }

    public String uri() {
        return uri;
    }

    public String body() {
        return body;
    }

    HttpUriRequest build() {
        return builder.build();
    }
}
