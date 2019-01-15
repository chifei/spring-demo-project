package core.framework.scheduler;

/**
 * @author neo
 */
public interface Job {
    void execute() throws Throwable;
}
