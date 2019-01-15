package core.framework.event;

/**
 * @author neo
 */
class EventUtils {
    static String type(Class<?> eventClass) {
        Event eventAnnotation = eventClass.getAnnotation(Event.class);
        if (eventAnnotation == null)
            throw new IllegalStateException("event does not have @Event, eventClass=" + eventClass);
        return eventAnnotation.value();
    }
}
