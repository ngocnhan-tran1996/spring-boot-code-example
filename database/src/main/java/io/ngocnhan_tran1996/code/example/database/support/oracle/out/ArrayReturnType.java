package io.ngocnhan_tran1996.code.example.database.support.oracle.out;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;

public class ArrayReturnType<T> extends AbstractSqlReturnType<T> {

    @Override
    protected Object convertArray(Connection connection, Array array) throws SQLException {

        return array.getArray();
    }

    @Override
    public int sqlType() {

        return Types.ARRAY;
    }

    @Override
    protected T convertStruct(Connection connection, Struct struct) throws SQLException {
        return null;
    }

}