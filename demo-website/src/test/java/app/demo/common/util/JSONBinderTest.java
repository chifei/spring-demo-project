package app.demo.common.util;


import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class JSONBinderTest {
    @Test
    public void supportJAXBAnnotations() {
        JSONTestBean bean = new JSONTestBean();
        bean.setField("value");

        String json = JSONBinder.toJSON(bean);

        assertThat(json, containsString("\"different-field\":\"value\""));
    }

    @Test
    public void fromJSON() {
        JSONTestBean bean = JSONBinder.fromJSON(JSONTestBean.class, "{\"different-field\":\"value\"}");

        assertEquals("value", bean.getField());
    }

    @Test
    public void fromJSONWithUnknownFields() {
        JSONTestBean bean = JSONBinder.fromJSON(JSONTestBean.class, "{\"different-field\":\"value\", \"non-existed-field\":\"someValue\"}");

        assertEquals("value", bean.getField());
    }

    @Test
    public void serializeDate() {
        JSONTestBean bean = new JSONTestBean();
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(2012, Calendar.APRIL, 18, 11, 30, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        bean.setDate(date);

        String json = JSONBinder.toJSON(bean);

        JSONTestBean result = JSONBinder.fromJSON(JSONTestBean.class, json);
        assertEquals(date, result.getDate());
    }

    @Test
    public void serializeListWithWrapper() {
        JSONTestBean bean = new JSONTestBean();
        JSONTestBeanItem item = new JSONTestBeanItem();
        item.setField("value");
        bean.getItems().add(item);

        String json = JSONBinder.toJSON(bean);
        assertThat(json, containsString("\"items\":[{\"field\":\"value\"}]"));
    }

    @Test
    public void serializeStringListWithWrapper() {
        JSONTestBean bean = new JSONTestBean();
        bean.getStringItems().add("value1");
        bean.getStringItems().add("value2");

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
        bean.setOffsetDateTime(OffsetDateTime.of(2012, Calendar.APRIL, 18, 11, 30, 0, 0, ZoneOffset.UTC));
        String json = JSONBinder.toJSON(bean);
        assertThat(json, containsString("\"offsetDateTime\":\"2012-03-18T11:30:00Z\""));
    }

    static enum TestEnum {
        A, B
    }

    @Test
    public void serializeEnum() {
        String value = JSONBinder.toJSON(TestEnum.A);
        assertEquals("\"A\"", value);

        assertEquals(TestEnum.A, JSONBinder.fromJSON(TestEnum.class, value));
    }
}
