package core.framework.log;

import ch.qos.logback.classic.PatternLayout;

import java.util.Map;

/**
 * @author neo
 */
public final class FilterMessagePatternLayout extends PatternLayout {
    private static final FilterMessagePatternLayout INSTANCE = new FilterMessagePatternLayout();

    private FilterMessagePatternLayout() {
        super();
        Map<String, String> converters = getInstanceConverterMap();
        converters.put("m", FilterMessageConverter.class.getName());
        converters.put("msg", FilterMessageConverter.class.getName());
        converters.put("message", FilterMessageConverter.class.getName());
        setPattern("%d [%thread] %-5level %logger{36} - %message%n");
    }

    public static FilterMessagePatternLayout get() {
        return INSTANCE;
    }
}
