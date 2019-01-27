package app.demo.common.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class JSONBinderThreadSafeTest {
    private static final int THREAD_COUNT = 5;

    JSONTestBean bean;
    Date date;

    @BeforeEach
    public void prepare() {
        bean = new JSONTestBean();
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(2012, Calendar.APRIL, 18, 11, 30, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        bean.date = date;
    }

    @Test
    public void jsonBinderShouldBeThreadSafe() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        final CountDownLatch executeLatch = new CountDownLatch(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread() {
                @Override
                public void run() {
                    serializeWithDateField(executeLatch, latch);
                }
            }.start();
        }

        boolean result = latch.await(30, TimeUnit.SECONDS);
        if (!result)
            fail("execution time out");
    }

    private void serializeWithDateField(CountDownLatch executeLatch, CountDownLatch latch) {
        try {
            executeLatch.countDown();
            executeLatch.await();

            String json = JSONBinder.toJSON(bean);
            assertThat(json, containsString("\"date\""));

            JSONTestBean result = JSONBinder.fromJSON(JSONTestBean.class, json);
            assertEquals(date, result.date);

            latch.countDown();
        } catch (Throwable throwable) {
            fail(throwable.getMessage());
        }
    }
}
