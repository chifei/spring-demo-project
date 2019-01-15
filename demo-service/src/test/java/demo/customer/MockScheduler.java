package demo.customer;

import core.framework.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * @author neo
 */
public class MockScheduler extends Scheduler {
    @Override
    public TaskScheduler scheduler() {
        return new MockTaskScheduler();
    }

    private static class MockTaskScheduler implements TaskScheduler {
        private final Logger logger = LoggerFactory.getLogger(MockTaskScheduler.class);

        @Override
        public ScheduledFuture schedule(Runnable task, Trigger trigger) {
            logger.info("schedule, job={}", task);
            return null;
        }

        @Override
        public ScheduledFuture schedule(Runnable task, Date startTime) {
            logger.info("schedule, job={}", task);
            return null;
        }

        @Override
        public ScheduledFuture scheduleAtFixedRate(Runnable task, Date startTime, long period) {
            logger.info("scheduleAtFixedRate, job={}, period={}", task, period);
            return null;
        }

        @Override
        public ScheduledFuture scheduleAtFixedRate(Runnable task, long period) {
            return scheduleAtFixedRate(task, new Date(), period);
        }

        @Override
        public ScheduledFuture scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
            logger.info("scheduleWithFixedDelay, job={}, delay={}", task, delay);
            return null;
        }

        @Override
        public ScheduledFuture scheduleWithFixedDelay(Runnable task, long delay) {
            return scheduleWithFixedDelay(task, new Date(), delay);
        }
    }
}
