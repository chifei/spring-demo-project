package core.framework.web.site.layout;

import core.framework.util.AssertUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author neo
 */
public class ModelContext {
    private final Map<String, Object> model = new HashMap<>();

    public Map<String, Object> getModel() {
        return model;
    }

    public void mergeToModel(Map<String, Object> model) {
        for (Map.Entry<String, Object> entry : this.model.entrySet()) {
            String key = entry.getKey();
            Object previousValue = model.put(key, entry.getValue());
            AssertUtils.assertNull(previousValue, "duplicated key found in model, key={}", key);
        }
    }
}
