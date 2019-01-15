package core.framework.template;

import core.framework.TestResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;

/**
 * @author neo
 */
public class FreemarkerTemplateTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    FreemarkerTemplate freemarkerTemplate;

    @Before
    public void createFreemarkerTemplate() {
        freemarkerTemplate = new FreemarkerTemplate();
        freemarkerTemplate.putTemplate("test", TestResource.text("/template-test/freemarker-template-test.ftl"));
    }

    @Test
    public void useObjectAsParameters() {
        Map<String, Object> context = new HashMap<>();
        context.put("name", "someName");

        Bean bean = new Bean();
        bean.name = "someBeanName";
        context.put("bean", bean);

        String result = freemarkerTemplate.transform("test", context);

        assertContainsString(result, "<name>someName</name>");
        assertContainsString(result, "<bean name=\"someBeanName\">");
        assertContainsString(result, "<method>someMessage</method>");
        assertContainsString(result, "<method-with-param>return-param</method-with-param>");
        assertContainsString(result, "<substring>so</substring>");
    }

    @Test
    public void variableIsNotDefined() {
        exception.expect(TemplateException.class);
        exception.expectMessage("The following has evaluated to null or missing");

        Map<String, Object> context = new HashMap<>();
        freemarkerTemplate.transform("test", context);
    }

    void assertContainsString(String actual, String subString) {
        Assert.assertThat(actual, containsString(subString));
    }

    public static class Bean {
        private String name;

        public String method() {
            return "someMessage";
        }

        public String methodWithParam(String param) {
            return "return-" + param;
        }

        public String getName() {
            return name;
        }
    }
}
