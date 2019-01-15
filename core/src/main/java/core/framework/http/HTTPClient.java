package core.framework.http;

import com.google.common.io.ByteStreams;
import core.framework.exception.RemoteServiceException;
import core.framework.util.CharacterEncodings;
import core.framework.util.TimeLength;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author neo
 */
public class HTTPClient {
    private final Logger logger = LoggerFactory.getLogger(HTTPClient.class);

    CloseableHttpClient client;
    boolean validateResponseStatus = false;
    private TimeLength timeOut = TimeLength.minutes(2);

    @PostConstruct
    public void shutdown() throws IOException {
        if (client != null)
            client.close();
    }

    public HTTPResponse execute(HTTPRequest request) {
        if (client == null) client = build();

        try (CloseableHttpResponse response = client.execute(request.build())) {
            int statusCode = response.getStatusLine().getStatusCode();
            String charset = responseCharset(response.getEntity());
            String responseText = new String(ByteStreams.toByteArray(response.getEntity().getContent()), charset);
            logger.debug("responseStatus={}", statusCode);
            for (Header header : response.getAllHeaders()) {
                logger.debug("[header] {}={}", header.getName(), header.getValue());
            }
            logger.debug("responseText={}", responseText);

            if (validateResponseStatus)
                validateResponseStatus(statusCode, responseText);

            return new HTTPResponse(statusCode, response.getAllHeaders(), responseText);
        } catch (IOException e) {
            throw new RemoteServiceException(e);
        }
    }

    private String responseCharset(HttpEntity responseEntity) {
        Header header = responseEntity.getContentType();
        if (header == null || header.getValue() == null) return CharacterEncodings.UTF_8;
        ContentType contentType = ContentType.parse(header.getValue());
        Charset charset = contentType.getCharset();
        if (charset == null) return CharacterEncodings.UTF_8;
        return charset.name();
    }

    private void validateResponseStatus(int statusCode, String responseBody) {
        if (statusCode < HttpStatus.SC_OK || statusCode > HttpStatus.SC_MULTI_STATUS)
            throw new RemoteServiceException("failed to call remote service, status=" + statusCode + ", response=" + responseBody);
    }

    private CloseableHttpClient build() {
        try {
            HttpClientBuilder builder = HttpClients.custom();
            builder.setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .setSslcontext(new SSLContextBuilder()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .build());
            // builder will use PoolingHttpClientConnectionManager by default
            builder.setDefaultSocketConfig(SocketConfig.custom()
                .setSoKeepAlive(true).build());
            builder.setDefaultRequestConfig(RequestConfig.custom()
                .setSocketTimeout((int) timeOut.toMilliseconds())
                .setConnectTimeout((int) timeOut.toMilliseconds()).build());
            return builder.build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new IllegalStateException(e);
        }
    }

    public void timeOut(TimeLength timeOut) {
        this.timeOut = timeOut;
    }

    public void validateResponseStatus(boolean validateResponseStatus) {
        this.validateResponseStatus = validateResponseStatus;
    }
}
