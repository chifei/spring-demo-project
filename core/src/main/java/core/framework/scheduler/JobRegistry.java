package core.framework.scheduler;

import com.amazonaws.services.sqs.AmazonSQS;
import core.framework.event.EventRegistry;
import core.framework.event.SQSEventListenerJob;
import core.framework.internal.SpringObjectFactory;
import core.framework.util.TimeLength;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.TimeZone;

/**
 * @author neo
 */
public class JobRegistry {
    private final ScheduledTaskRegistrar schedulerRegistry;
    private final SpringObjectFactory springObjectFactory;

    public JobRegistry(ScheduledTaskRegistrar schedulerRegistry, SpringObjectFactory springObjectFactory) {
        this.schedulerRegistry = schedulerRegistry;
        this.springObjectFactory = springObjectFactory;
    }

    public void triggerWithFixedDelay(Job job, TimeLength delay) {
        schedulerRegistry.addFixedDelayTask(jobProxy(job), delay.toMilliseconds());
    }

    public void triggerAtFixedRate(Job job, TimeLength interval) {
        schedulerRegistry.addFixedRateTask(jobProxy(job), interval.toMilliseconds());
    }

    public void triggerByCronExpression(Job job, String cronExpression) {
        triggerByCronExpression(job, cronExpression, TimeZone.getDefault());
    }

    public void triggerByCronExpression(Job job, String cronExpression, TimeZone timeZone) {
        schedulerRegistry.addCronTask(new CronTask(jobProxy(job), new CronTrigger(cronExpression, timeZone)));
    }

    public EventRegistry listenSQSEvents(AmazonSQS sqs, String queueURL) {
        SQSEventListenerJob job = new SQSEventListenerJob(sqs, queueURL);
        triggerWithFixedDelay(job, TimeLength.seconds(30));
        return new EventRegistry(job, springObjectFactory);
    }

    private JobProxy jobProxy(Job job) {
        return springObjectFactory.initialize(new JobProxy(springObjectFactory.initialize(job)));
    }
}
