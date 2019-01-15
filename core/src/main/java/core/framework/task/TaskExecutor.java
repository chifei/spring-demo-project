package core.framework.task;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import core.framework.exception.ErrorHandler;
import core.framework.internal.SpringObjectFactory;
import core.framework.log.TraceLogger;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author neo
 */
public final class TaskExecutor {
    public final ExecutorService executorService;

    @Inject
    SpringObjectFactory springObjectFactory;
    @Inject
    TraceLogger traceLogger;
    @Inject
    ErrorHandler errorHandler;

    private TaskExecutor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static TaskExecutor fixedSizeExecutor(int threadPoolSize) {
        return new TaskExecutor(Executors.newFixedThreadPool(threadPoolSize));
    }

    public static TaskExecutor unlimitedExecutor() {
        return new TaskExecutor(Executors.newCachedThreadPool());
    }

    public static TaskExecutor parallelExecutor(int parallelism) {
        return new TaskExecutor(Executors.newWorkStealingPool(parallelism));
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }

    public <T> Future<T> submit(Callable<T> task) {
        TaskProxy<T> proxy = taskProxy(task);
        return executorService.submit(proxy);
    }

    public <T> List<T> executeAll(List<? extends Callable<T>> tasks) {
        try {
            List<Callable<T>> proxyTasks = Lists.transform(tasks, new Function<Callable<T>, Callable<T>>() {
                @Override
                public Callable<T> apply(Callable<T> task) {
                    return taskProxy(task);
                }
            });
            List<Future<T>> futures = executorService.invokeAll(proxyTasks);
            List<T> results = new ArrayList<>(futures.size());
            for (Future<T> future : futures) {
                results.add(future.get());
            }
            return results;
        } catch (InterruptedException | ExecutionException e) {
            throw new TaskExecutionException(e);
        }
    }

    private <T> TaskProxy<T> taskProxy(Callable<T> task) {
        Callable<T> taskBean = springObjectFactory.initialize(task);
        return new TaskProxy<>(taskBean, traceLogger, errorHandler);
    }
}
