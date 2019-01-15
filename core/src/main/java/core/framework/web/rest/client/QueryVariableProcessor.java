package core.framework.web.rest.client;

import core.framework.util.EncodingUtils;

import java.util.List;

/**
 * @author neo
 */
public class QueryVariableProcessor {
    private final VariablePosition[] positions;

    QueryVariableProcessor(List<VariablePosition> positions) {
        this.positions = positions.toArray(new VariablePosition[positions.size()]);
    }

    String urlParams(Object[] arguments) {
        StringBuilder builder = new StringBuilder("?");
        int index = 0;
        for (VariablePosition position : positions) {
            if (index > 0) {
                builder.append('&');
            }
            builder.append(position.name)
                .append('=')
                .append(EncodingUtils.url(String.valueOf(arguments[position.index])));

            index++;
        }
        return builder.toString();
    }
}
