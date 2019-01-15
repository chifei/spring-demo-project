import core.framework.http.HTTPClient;
import core.framework.web.rest.client.APIClientBuilder;
import demo.customer.web.CustomerWebService;
import demo.customer.web.request.RegisterCustomerRequest;
import demo.customer.web.response.CustomerResponse;

/**
 * @author neo
 */
public class CustomerServiceClient {
    public static void main(String[] args) {
//        JedisPoolConfig config = new JedisPoolConfig();
//
//        JedisPool pool = new JedisPool(config, "ec2-54-227-55-127.compute-1.amazonaws.com");
//        Jedis redis = pool.getResource();
//        redis.expire("key", 30);


        CustomerWebService client = new APIClientBuilder("http://localhost:8080", new HTTPClient())
            .signBy(request -> {
                request.header("request-id", "123");
                String body = request.body();
                System.out.println(body);
            })
            .build(CustomerWebService.class);

        RegisterCustomerRequest request = new RegisterCustomerRequest();
        request.name = "test";
        request.email = "test@test.com";
        CustomerResponse response = client.create(request);

        CustomerResponse customerResponse = client.get(1l);
        System.out.println();
    }
}
