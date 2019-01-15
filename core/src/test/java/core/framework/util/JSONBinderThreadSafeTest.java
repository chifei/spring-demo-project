package core.framework.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author neo
 */
public class JSONBinderThreadSafeTest {
    private static final int THREAD_COUNT = 5;

    JSONTestBean bean;
    Date date;

    @Before
    public void prepare() {
        bean = new JSONTestBean();
        date = DateUtils.date(2012, Calendar.APRIL, 18, 11, 30, 0);
        bean.setDate(date);
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
            Assert.fail("execution time out");
    }

    private void serializeWithDateField(CountDownLatch executeLatch, CountDownLatch latch) {
        try {
            executeLatch.countDown();
            executeLatch.await();

            String json = JSONBinder.toJSON(bean);
            assertThat(json, containsString("\"date\""));

            JSONTestBean result = JSONBinder.fromJSON(JSONTestBean.class, json);
            Assert.assertEquals(date, result.getDate());

            latch.countDown();
        } catch (Throwable throwable) {
            Assert.fail(throwable.getMessage());
        }
    }
}
