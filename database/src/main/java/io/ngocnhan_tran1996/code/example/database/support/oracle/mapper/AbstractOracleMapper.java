package io.ngocnhan_tran1996.code.example.database.support.oracle.mapper;

import io.ngocnhan_tran1996.code.example.database.support.oracle.exception.OracleTypeException;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.OracleTypeUtils;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Map;
import oracle.jdbc.OracleDatabaseMetaData;
import oracle.jdbc.OracleTypeMetaData;
import oracle.jdbc.driver.OracleConnection;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

abstract class AbstractOracleMapper<T> implements OracleMapper<T> {

    /**
     * Extract the values for all columns in the current row.
     * <p>
     * Utilizes public setters and result set meta-data.
     *
     * @see java.sql.ResultSetMetaData
     */
    @Override
    public Struct toStruct(Connection connection, T source, String typeName) throws SQLException {

        var oracleTypeMetaData = connection.getMetaData()
            .unwrap(OracleDatabaseMetaData.class)
            .getOracleTypeMetaData(typeName);

        if (oracleTypeMetaData.getKind() != OracleTypeMetaData.Kind.STRUCT) {

            throw new OracleTypeException(String.format("%s is not struct", typeName));
        }

        ResultSetMetaData rsmd = this.getResultSetMetaData(oracleTypeMetaData);
        Object[] objects = this.createStruct(
            rsmd.getColumnCount(),
            this.extractIndexByColumnName(rsmd),
            new BeanWrapperImpl(source));
        return connection.unwrap(OracleConnection.class)
            .createStruct(typeName, objects);
    }

    /**
     * Extract the values for all attributes in the struct.
     * <p>
     * Utilizes public setters and result set metadata.
     *
     * @see java.sql.ResultSetMetaData
     */
    @Override
    public T fromStruct(Connection connection, Struct struct) {

        try {

            var typeName = struct.getSQLTypeName();
            var oracleTypeMetaData = connection.getMetaData()
                .unwrap(OracleDatabaseMetaData.class)
                .getOracleTypeMetaData("USER_NHAN.JDBC_EXAMPLE_PACK.NAME_RECORD");

            if (oracleTypeMetaData.getKind() != OracleTypeMetaData.Kind.STRUCT) {

                throw new OracleTypeException(String.format("%s is not struct", typeName));
            }

            ResultSetMetaData rsmd = this.getResultSetMetaData(oracleTypeMetaData);
            Object[] values = struct.getAttributes();
            var valueByName = new LinkedCaseInsensitiveMap<Object>();
            this.extractIndexByColumnName(rsmd)
                .forEach((columnName, index) -> valueByName.put(columnName, values[index]));

            return this.constructMappedInstance(valueByName, new BeanWrapperImpl());
        } catch (SQLException e) {

            return null;
        }
    }

    @Override
    public T convert(Map<String, Object> source) {

        OracleTypeUtils.throwIfNull(source);
        var valueByName = new LinkedCaseInsensitiveMap<Object>();
        source.forEach((columnName, value) -> this.putValueByColumnName(
            valueByName,
            columnName,
            value));
        return this.constructMappedInstance(valueByName, new BeanWrapperImpl());
    }

    protected abstract Object[] createStruct(
        int columns,
        Map<String, Integer> columnNameByIndex,
        BeanWrapperImpl bw);

    protected abstract T constructMappedInstance(Map<String, Object> valueByName,
        BeanWrapperImpl bw);

    private ResultSetMetaData getResultSetMetaData(OracleTypeMetaData struct)
        throws SQLException {

        return ((OracleTypeMetaData.Struct) struct).getMetaData();
    }

    private Map<String, Integer> extractIndexByColumnName(ResultSetMetaData rsmd)
        throws SQLException {

        Map<String, Integer> indexByColumnName = new LinkedCaseInsensitiveMap<>();
        var columns = rsmd.getColumnCount();
        for (int i = 0; i < columns; i++) {

            String columnName = JdbcUtils.lookupColumnName(rsmd, i + 1);
            this.putValueByColumnName(indexByColumnName, columnName, i);
        }

        return indexByColumnName;
    }

    private <V> void putValueByColumnName(
        Map<String, V> valuexByColumnName,
        String columnName,
        V value) {

        valuexByColumnName.put(columnName, value);
        String propertyName = JdbcUtils.convertUnderscoreNameToPropertyName(columnName);
        if (Strings.notEquals(columnName, propertyName)) {

            valuexByColumnName.put(propertyName, value);
        }
    }

}