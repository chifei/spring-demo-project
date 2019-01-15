package core.framework.event;

import core.framework.internal.SpringObjectFactory;
import core.framework.util.AssertUtils;

/**
 * @author neo
 */
public class EventRegistry {
    final SQSEventListenerJob job;
    final SpringObjectFactory springObjectFactory;

    public EventRegistry(SQSEventListenerJob job, SpringObjectFactory springObjectFactory) {
        this.job = job;
        this.springObjectFactory = springObjectFactory;
    }

    public <T> EventRegistry subscribe(Class<T> eventClass, EventHandler<T> eventHandler) {
        EventHandler<T> handler = springObjectFactory.initialize(eventHandler);
        job.subscribe(eventClass, handler);
        return this;
    }

    public <T> EventRegistry maxConcurrentHandlers(int maxConcurrentHandlers) {
        AssertUtils.assertTrue(maxConcurrentHandlers > 0, "maxConcurrentHandlers should be greater than 0");
        job.maxConcurrentHandlers = maxConcurrentHandlers;
        return this;
    }
}
