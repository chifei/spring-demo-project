package core.framework.database;

import core.framework.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author neo
 */
public class ResultSets {
    private static final String RESULT_KEY_UPDATE_COUNT = "update-count";

    private final Logger logger = LoggerFactory.getLogger(ResultSets.class);
    private final Map<String, Object> results;

    public ResultSets(Map<String, Object> results) {
        this.results = results;
        logResults(results);
    }

    private void logResults(Map<String, Object> results) {
        logger.debug("results, totalResultSets={}", results.size());
        for (Entry<String, Object> entry : results.entrySet()) {
            int count = 1;
            Object value = entry.getValue();
            if (value instanceof List)
                count = ((List) value).size();
            logger.debug("results, name={}, count={}", entry.getKey(), count);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> resultSet(int index) {
        List<T> resultSet = (List<T>) results.get(StoredProcedure.buildResultSetIndex(index));
        AssertUtils.assertNotNull(resultSet, "sp didn't return expected resultSet, index={}", index);
        return resultSet;
    }

    @SuppressWarnings("unchecked")
    public <T> T uniqueResult(int index) {
        List<Object> resultSet = resultSet(index);
        if (resultSet.size() > 1)
            throw new IncorrectResultSizeDataAccessException(1, resultSet.size());
        if (resultSet.isEmpty())
            return null;
        return (T) resultSet.get(0);
    }

    @SuppressWarnings("unchecked")
    public <T> T outParamValue(String param) {
        return (T) results.get(param);
    }

    public int totalUpdateCount() {
        int count = 0;

        for (Entry<String, Object> entry : results.entrySet()) {
            if (entry.getKey().contains(RESULT_KEY_UPDATE_COUNT)) {
                count += (Integer) entry.getValue();
            }
        }

        return count;
    }
}
