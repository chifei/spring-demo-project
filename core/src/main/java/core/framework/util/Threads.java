package core.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author neo
 */
public final class Threads {
    private static final Logger LOGGER = LoggerFactory.getLogger(Threads.class);
    private static final Random RANDOM = new Random();

    public static void sleepRoughly(TimeLength duration) throws InterruptedException {
        long milliseconds = duration.toMilliseconds();
        double times = 1 + RANDOM.nextDouble() / 10 * 4 - 0.2; // +/-20% random
        long sleepTime = (long) (milliseconds * times);
        LOGGER.info("sleep {} milliseconds", sleepTime);
        Thread.sleep(sleepTime);
    }
}
