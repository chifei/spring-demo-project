package core.framework.web.rest.client;

import com.google.common.collect.Lists;
import core.framework.http.HTTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.List;

/**
 * @author neo
 */
public class APIClientsBuilder implements BeanDefinitionRegistryPostProcessor {
    private final Logger logger = LoggerFactory.getLogger(APIClientsBuilder.class);
    final String serviceURL;
    final HTTPClient httpClient;
    final List<Class<?>> serviceClasses = Lists.newArrayList();
    RequestSigner requestSigner;

    public APIClientsBuilder(String serviceURL, HTTPClient httpClient) {
        this.serviceURL = serviceURL;
        this.httpClient = httpClient;
    }

    public APIClientsBuilder signBy(RequestSigner requestSigner) {
        this.requestSigner = requestSigner;
        return this;
    }

    public APIClientsBuilder add(Class<?> serviceClass) {
        serviceClasses.add(serviceClass);
        return this;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (Class<?> serviceClass : serviceClasses) {
            logger.info("registered api client, class={}, url={}", serviceClass, serviceURL);
            Object serviceClient = new APIClientBuilder(serviceURL, httpClient)
                .signBy(requestSigner)
                .build(serviceClass);

            beanFactory.registerSingleton(serviceClass.getName(), serviceClient);
        }
    }
}
