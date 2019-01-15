package core.framework.database;

import core.framework.util.AssertUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import javax.persistence.Column;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * use JPA @Column to map db column name to field, only supports fields
 *
 * @author neo
 */
public final class EntityRowMapper<T> implements RowMapper<T> {
    private final Constructor<T> constructor;
    private final Map<String, Field> columnMappings = new HashMap<>();
    private Map<Integer, Field> resultSetFieldMappings;

    private EntityRowMapper(Class<T> entityClass) {
        constructor = getConstructor(entityClass);

        initializeColumnMappings(entityClass);
    }

    public static <T> EntityRowMapper<T> rowMapper(Class<T> entityClass) {
        return new EntityRowMapper<>(entityClass);
    }

    @Override
    public T mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        if (resultSetFieldMappings == null) {
            resultSetFieldMappings = buildResultSetFieldMappings(resultSet);
        }
        try {
            T result = constructor.newInstance();
            assignColumnValues(resultSet, result, resultSetFieldMappings);
            return result;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new SQLException("failed to create instance, constructor=" + constructor, e);
        }
    }

    private Map<Integer, Field> buildResultSetFieldMappings(ResultSet resultSet) throws SQLException {
        Map<Integer, Field> resultSetFieldMappings = new HashMap<>();
        ResultSetMetaData meta = resultSet.getMetaData();
        int count = meta.getColumnCount();
        for (int i = 1; i < count + 1; i++) {
            String column = meta.getColumnName(i);
            Field field = columnMappings.get(column.toLowerCase());
            if (field != null) {
                resultSetFieldMappings.put(i, field);
            }
        }

        return resultSetFieldMappings;
    }

    private void initializeColumnMappings(Class<T> entityClass) {
        Class<?> targetClass = entityClass;
        while (!Object.class.equals(targetClass)) {
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    String columnName = column.name().toLowerCase();
                    field.setAccessible(true);
                    Field previousValue = columnMappings.put(columnName, field);
                    AssertUtils.assertNull(previousValue, "columnName was defined, previousField1={}, field={}", previousValue, field);
                }
            }

            targetClass = targetClass.getSuperclass();
        }
    }

    private Constructor<T> getConstructor(Class<T> entityClass) {
        AssertUtils.assertNotNull(entityClass, "entityClass can not be null");
        try {
            return entityClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("entityClass must have empty constructor", e);
        }
    }

    private void assignColumnValues(ResultSet resultSet, T result, Map<Integer, Field> resultSetFieldMappings) throws SQLException, IllegalAccessException {
        for (Map.Entry<Integer, Field> entry : resultSetFieldMappings.entrySet()) {
            Field field = entry.getValue();
            Object value = JdbcUtils.getResultSetValue(resultSet, entry.getKey(), field.getType());

            field.set(result, value);
        }
    }
}
