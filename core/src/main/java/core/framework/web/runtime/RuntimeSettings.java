package core.framework.web.runtime;

import core.framework.util.StringUtils;

/**
 * @author neo
 */
public class RuntimeSettings {
    private RuntimeEnvironment environment = RuntimeEnvironment.DEV;
    private String version = "current";

    public RuntimeEnvironment environment() {
        return environment;
    }

    public RuntimeSettings setEnvironment(RuntimeEnvironment environment) {
        this.environment = environment;
        return this;
    }

    public String version() {
        return version;
    }

    public RuntimeSettings setVersion(String version) {
        if (!StringUtils.hasText(version) || version.contains("${")) {
            // use current if version is from property variable
            this.version = "current";
        } else {
            this.version = version;
        }
        return this;
    }
}
