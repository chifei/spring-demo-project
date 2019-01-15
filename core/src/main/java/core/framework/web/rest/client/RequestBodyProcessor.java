package core.framework.web.rest.client;

import core.framework.util.JSONBinder;

/**
 * @author neo
 */
class RequestBodyProcessor {
    private final int paramIndex;

    RequestBodyProcessor(int paramIndex) {
        this.paramIndex = paramIndex;
    }

    String body(Object[] params) {
        return JSONBinder.toJSON(params[paramIndex]);
    }
}
