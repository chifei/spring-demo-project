package core.test.rest.internal;

import core.framework.internal.ClassUtils;
import core.framework.scheduler.Job;
import core.test.rest.SpringTest;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author neo
 */
public class ClassUtilsTest extends SpringTest {
    @Inject
    ClassUtilsTestProxyBean classUtilsTestProxyBean;

    @Test
    public void getClassNameForProxyBean() {
        String className = ClassUtils.getOriginalClassName(classUtilsTestProxyBean);
        Assert.assertEquals(ClassUtilsTestProxyBean.class.getName(), className);
    }

    @Test
    public void getClassNameForNormalBean() {
        String className = ClassUtils.getOriginalClassName(new ClassUtilsTestProxyBean());
        Assert.assertEquals(ClassUtilsTestProxyBean.class.getName(), className);
    }

    @Test
    public void getSimpleClassNameForNormalBean() {
        String className = ClassUtils.getSimpleOriginalClassName(new ClassUtilsTestProxyBean());
        Assert.assertEquals("ClassUtilsTestProxyBean", className);
    }

    @Test
    public void getSimpleClassNameForInnerClassBean() {
        String className = ClassUtils.getSimpleOriginalClassName(new Job() {
            @Override
            public void execute() throws Throwable {
            }
        });
        Assert.assertEquals("ClassUtilsTest$1", className);
    }
}
