package core.framework.event;

/**
 * @author neo
 */
public interface EventHandler<T> {
    void handle(T event) throws Throwable;
}
