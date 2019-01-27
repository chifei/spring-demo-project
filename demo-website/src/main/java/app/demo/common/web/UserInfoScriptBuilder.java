package app.demo.common.web;

import com.google.common.collect.Maps;

import java.util.Map;


public class UserInfoScriptBuilder {
    private final UserInfo userInfo;

    public UserInfoScriptBuilder(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Map<String, Object> build() {
        Map<String, Object> json = Maps.newHashMap();
        json.put("username", userInfo.username());
        json.put("permissions", userInfo.permissions());
        return json;
    }
}
