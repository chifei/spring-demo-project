package core.framework.database;

import core.framework.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author neo
 */
public class JDBCAccess {
    private final Logger logger = LoggerFactory.getLogger(JDBCAccess.class);
    private JdbcTemplate jdbcTemplate;

    public <T> List<T> find(String sql, RowMapper<T> rowMapper, Object... params) {
        StopWatch watch = new StopWatch();
        int returnedRows = 0;
        try {
            List<T> results = jdbcTemplate.query(sql, params, rowMapper);
            returnedRows = results.size();
            return results;
        } finally {
            logger.debug("find, sql={}, params={}, returnedRows={}, elapsedTime={}", sql, params, returnedRows, watch.elapsedTime());
        }
    }

    public <T> T findOne(String sql, RowMapper<T> rowMapper, Object... params) {
        StopWatch watch = new StopWatch();
        try {
            return jdbcTemplate.queryForObject(sql, params, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("findOne did not find any result", e);
            return null;
        } finally {
            logger.debug("findOne, sql={}, params={}, elapsedTime={}", sql, params, watch.elapsedTime());
        }
    }

    public Integer findInteger(String sql, Object... params) {
        StopWatch watch = new StopWatch();
        try {
            Number number = jdbcTemplate.queryForObject(sql, params, Integer.class);
            return number != null ? number.intValue() : 0;
        } catch (EmptyResultDataAccessException e) {
            logger.debug("findInteger did not find any result", e);
            return null;
        } finally {
            logger.debug("findInteger, sql={}, params={}, elapsedTime={}", sql, params, watch.elapsedTime());
        }
    }

    public String findString(String sql, Object... params) {
        StopWatch watch = new StopWatch();
        try {
            return jdbcTemplate.queryForObject(sql, params, new SimpleRowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                    return resultSet.getString(1);
                }
            });
        } catch (EmptyResultDataAccessException e) {
            logger.debug("findString did not find any result", e);
            return null;
        } finally {
            logger.debug("findString, sql={}, params={}, elapsedTime={}", sql, params, watch.elapsedTime());
        }
    }

    public int execute(String sql, Object... params) {
        StopWatch watch = new StopWatch();
        int updatedRows = 0;
        try {
            updatedRows = jdbcTemplate.update(sql, params);
            return updatedRows;
        } finally {
            logger.debug("execute, sql={}, params={}, updatedRows={}, elapsedTime={}", sql, params, updatedRows, watch.elapsedTime());
        }
    }

    public int[] batchExecute(String sql, List<Object[]> params) {
        StopWatch watch = new StopWatch();
        int totalUpdatedRows = 0;
        try {
            int[] results = jdbcTemplate.batchUpdate(sql, params);
            for (int updatedRows : results) {
                totalUpdatedRows += updatedRows;
            }
            return results;
        } finally {
            logger.debug("batchExecute, sql={}, params={}, totalUpdatedRows={}, elapsedTime={}", sql, params, totalUpdatedRows, watch.elapsedTime());
        }
    }

    // for calling sp without named out parameter, will preserve the order of rowMappers, use regular query if only return single result set
    public ResultSets callSP(StoredProcedure storedProcedure) {
        StopWatch watch = new StopWatch();
        try {
            return storedProcedure.execute(jdbcTemplate);
        } finally {
            logger.debug("callSP, sp={}, elapsedTime={}", storedProcedure.name, watch.elapsedTime());
        }
    }

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
