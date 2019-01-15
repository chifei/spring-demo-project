package core.framework.scheduler;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author neo
 */
public class Scheduler {
    private TaskScheduler scheduler;
    private ScheduledExecutorService executor;

    @PreDestroy
    public void shutdown() {
        if (executor != null)
            executor.shutdown();
    }

    public TaskScheduler scheduler() {
        if (scheduler == null) {
            executor = Executors.newScheduledThreadPool(10);
            scheduler = new ConcurrentTaskScheduler(executor);
        }
        return scheduler;
    }
}
