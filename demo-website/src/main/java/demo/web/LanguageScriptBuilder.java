package demo.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class LanguageScriptBuilder {
    public List<Map<String, String>> build() {
        Map<String, String> en = Maps.newHashMap();
        en.put("language", "en-US");
        en.put("displayName", "English");

        Map<String, String> zh = Maps.newHashMap();
        zh.put("language", "zh-CN");
        zh.put("displayName", "中文");
        return Lists.newArrayList(en, zh);
    }
}
