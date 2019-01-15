package core.framework.web.site.url;

/**
 * @author neo
 */
public class URLParam {
    private final String name;
    private final String value;

    public URLParam(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
