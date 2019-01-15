package core.framework.web.rest.client;

import java.util.List;

/**
 * @author neo
 */
class PathVariableProcessor {
    private final VariablePosition[] positions;

    PathVariableProcessor(List<VariablePosition> positions) {
        this.positions = positions.toArray(new VariablePosition[positions.size()]);
    }

    String url(String pattern, Object[] arguments) {
        String url = pattern;
        for (VariablePosition position : positions) {
            url = url.replace("{" + position.name + "}", String.valueOf(arguments[position.index]));
        }
        return url;
    }
}
