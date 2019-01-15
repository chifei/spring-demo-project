package core.framework.scheduler;

import core.framework.internal.SpringObjectFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.inject.Inject;

/**
 * @author neo
 */
@EnableScheduling
public abstract class DefaultSchedulerConfig implements SchedulingConfigurer {
    @Inject
    SpringObjectFactory springObjectFactory;
    @Inject
    Scheduler scheduler;

    @Override
    public void configureTasks(ScheduledTaskRegistrar registry) {
        registry.setScheduler(scheduler.scheduler());
        try {
            configure(new JobRegistry(registry, springObjectFactory));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("failed to configure scheduler jobs, errorMessage=" + e.getMessage(), e);
        }
    }

    protected abstract void configure(JobRegistry registry) throws Exception;
}
