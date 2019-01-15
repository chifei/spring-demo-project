package core.framework.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author neo
 */
public class StoredProcedure {
    private final Logger logger = LoggerFactory.getLogger(StoredProcedure.class);
    final String name;
    private String catalog;
    private final Map<String, RowMapper> rowMappers = new LinkedHashMap<>();
    private final Map<String, Object> params = new HashMap<>();
    private int resultSetIndex = 0;

    public StoredProcedure(String name) {
        this.name = name;
    }

    static String buildResultSetIndex(int index) {
        return "result-set-" + index;
    }

    public StoredProcedure addRowMapper(RowMapper rowMapper) {
        rowMappers.put(buildResultSetIndex(resultSetIndex), rowMapper);
        resultSetIndex++;
        return this;
    }

    public StoredProcedure addOutParamRowMapper(String param, RowMapper rowMapper) {
        rowMappers.put(param.toLowerCase(), rowMapper);
        return this;
    }

    public StoredProcedure param(String param, Object value) {
        params.put(param, value);
        return this;
    }

    ResultSets execute(JdbcTemplate jdbcTemplate) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName(name);

        if (catalog != null) jdbcCall.withCatalogName(catalog);

        for (Map.Entry<String, RowMapper> entry : rowMappers.entrySet()) {
            jdbcCall.returningResultSet(entry.getKey(), entry.getValue());
        }
        Map<String, Object> results = jdbcCall.execute(params);
        logger.debug("execute jdbc call, sp={}, params={}", name, params);
        return new ResultSets(results);
    }

    public StoredProcedure catalog(String catalog) {
        this.catalog = catalog;
        return this;
    }
}
