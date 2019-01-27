package app.demo.common.util;


import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class JSONBinderTest {
    @Test
    public void supportJAXBAnnotations() {
        JSONTestBean bean = new JSONTestBean();
        bean.field = "value";

        String json = JSONBinder.toJSON(bean);

        assertThat(json, containsString("\"different-field\":\"value\""));
    }

    @Test
    public void fromJSON() {
        JSONTestBean bean = JSONBinder.fromJSON(JSONTestBean.class, "{\"different-field\":\"value\"}");

        assertEquals("value", bean.field);
    }

    @Test
    public void fromJSONWithUnknownFields() {
        JSONTestBean bean = JSONBinder.fromJSON(JSONTestBean.class, "{\"different-field\":\"value\", \"non-existed-field\":\"someValue\"}");

        assertEquals("value", bean.field);
    }

    @Test
    public void serializeDate() {
        JSONTestBean bean = new JSONTestBean();
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(2012, Calendar.APRIL, 18, 11, 30, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        bean.date = calendar.getTime();

        String json = JSONBinder.toJSON(bean);

        JSONTestBean result = JSONBinder.fromJSON(JSONTestBean.class, json);
        assertEquals(calendar.getTime(), result.date);
    }

    @Test
    public void serializeListWithWrapper() {
        JSONTestBean bean = new JSONTestBean();
        JSONTestBeanItem item = new JSONTestBeanItem();
        item.field = "value";
        bean.items.add(item);

        String json = JSONBinder.toJSON(bean);
        assertThat(json, containsString("\"items\":[{\"field\":\"value\"}]"));
    }

    @Test
    public void serializeStringListWithWrapper() {
        JSONTestBean bean = new JSONTestBean();
        bean.stringItems.add("value1");
        bean.stringItems.add("value2");

        String json = JSONBinder.toJSON(bean);
        assertThat(json, containsString("\"string-items\":[\"value1\",\"value2\"]"));
    }

    @Test
    public void handleNull() {
        String value = JSONBinder.toJSON(null);
        assertEquals("null", value);

        JSONTestBean bean = JSONBinder.fromJSON(JSONTestBean.class, value);
        assertNull(bean);
    }

    @Test
    public void serializeInteger() {
        String value = JSONBinder.toJSON(1);
        assertEquals("1", value);

        assertEquals(1, (int) JSONBinder.fromJSON(Integer.class, value));
    }

    @Test
    public void serializeString() {
        String value = JSONBinder.toJSON("value");
        assertEquals("\"value\"", value);

        assertEquals("value", JSONBinder.fromJSON(String.class, value));
    }

    @Test
    public void serializeOffsetDateTime() {
        JSONTestBean bean = new JSONTestBean();
        bean.offsetDateTime = OffsetDateTime.of(2012, Calendar.APRIL, 18, 11, 30, 0, 0, ZoneOffset.UTC);
        String json = JSONBinder.toJSON(bean);
        assertThat(json, containsString("\"offsetDateTime\":\"2012-03-18T11:30:00Z\""));
    }

    @Test
    public void serializeEnum() {
        String value = JSONBinder.toJSON(TestEnum.A);
        assertEquals("\"A\"", value);

        assertEquals(TestEnum.A, JSONBinder.fromJSON(TestEnum.class, value));
    }

    enum TestEnum {
        A, B
    }
}
