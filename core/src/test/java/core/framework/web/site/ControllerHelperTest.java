package core.framework.web.site;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author neo
 */
public class ControllerHelperTest {
    @Test
    public void findClassLevelAnnotationOnParentClass() throws NoSuchMethodException {
        HandlerMethod handler = new HandlerMethod(new TestControllerWithClassAnnotation(), TestControllerWithClassAnnotation.class.getDeclaredMethod("execute"));

        TestClassLevelAnnotation annotation = ControllerHelper.findMethodOrClassLevelAnnotation(handler, TestClassLevelAnnotation.class);

        Assert.assertNotNull(annotation);
    }

    @Test
    public void notFindClassLevelAnnotation() throws NoSuchMethodException {
        HandlerMethod handler = new HandlerMethod(new TestControllerWithMethodAnnotation(), TestControllerWithMethodAnnotation.class.getDeclaredMethod("execute"));

        TestClassLevelAnnotation annotation = ControllerHelper.findMethodOrClassLevelAnnotation(handler, TestClassLevelAnnotation.class);

        Assert.assertNull(annotation);
    }

    @Test
    public void findMethodLevelAnnotation() throws NoSuchMethodException {
        HandlerMethod handler = new HandlerMethod(new TestControllerWithMethodAnnotation(), TestControllerWithMethodAnnotation.class.getDeclaredMethod("execute"));

        TestMethodLevelAnnotation annotation = ControllerHelper.findMethodOrClassLevelAnnotation(handler, TestMethodLevelAnnotation.class);

        Assert.assertNotNull(annotation);
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestClassLevelAnnotation {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestMethodLevelAnnotation {
    }

    @TestClassLevelAnnotation
    public static class TestParentController {

    }

    public static class TestControllerWithClassAnnotation extends TestParentController {
        public String execute() {
            return "view";
        }
    }

    public static class TestControllerWithMethodAnnotation {
        @TestMethodLevelAnnotation
        public String execute() {
            return "view";
        }
    }
}
