package demo.web;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class MessageBundleBuilder {
    private final Map<String, String> messageBundle;

    public MessageBundleBuilder(Map<String, String> messageBundle) {
        this.messageBundle = messageBundle;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> build() {
        Map<String, Object> json = Maps.newHashMap();
        messageBundle.keySet().forEach(key -> {
            List<String> keys = Splitter.on('.').splitToList(key);
            String value = messageBundle.get(key);
            Map<String, Object> current = json;
            for (int i = 0; i < keys.size(); i++) {
                String k = keys.get(i);
                if (i == keys.size() - 1) {
                    current.put(k, value);
                } else {
                    Map next = (Map<String, Object>) current.get(k);
                    if (next == null) {
                        next = Maps.newHashMap();
                        current.put(k, next);
                    }
                    current = next;
                }
            }
        });
        return json;
    }
}
