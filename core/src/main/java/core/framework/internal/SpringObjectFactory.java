package core.framework.internal;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author neo
 */
public class SpringObjectFactory {
    @Inject
    ApplicationContext applicationContext;

    public <T> T create(Class<T> beanClass) {
        return applicationContext.getAutowireCapableBeanFactory().createBean(beanClass);
    }

    public <T> T bean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    public <T> List<T> beans(Class<T> beanClass) {
        return new ArrayList<>(applicationContext.getBeansOfType(beanClass).values());
    }

    public void registerSingletonBean(String beanName, Class beanClass) {
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(beanClass);
        definition.setScope(BeanDefinition.SCOPE_SINGLETON);
        AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        if (factory instanceof BeanDefinitionRegistry) {
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
            registry.registerBeanDefinition(beanName, definition);
        } else {
            throw new IllegalStateException("spring AutowireCapableBeanFactory should be BeanDefinitionRegistry");
        }
    }

    public void registerSingletonBean(String beanName, Object bean) {
        SingletonBeanRegistry registry = (SingletonBeanRegistry) applicationContext.getAutowireCapableBeanFactory();
        registry.registerSingleton(beanName, bean);
    }

    @SuppressWarnings("unchecked")
    public <T> T initialize(T bean) {
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(bean);
        return (T) beanFactory.initializeBean(bean, bean.getClass().getName());
    }
}
