package core.framework.http;

import org.apache.http.Header;

/**
 * @author neo
 */
public class HTTPResponse {
    final int statusCode;
    final Header[] headers;
    final String responseText;

    public HTTPResponse(int statusCode, Header[] headers, String responseText) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.responseText = responseText;
    }

    public int statusCode() {
        return statusCode;
    }

    public Header[] headers() {
        return headers;
    }

    public String responseText() {
        return responseText;
    }
}
