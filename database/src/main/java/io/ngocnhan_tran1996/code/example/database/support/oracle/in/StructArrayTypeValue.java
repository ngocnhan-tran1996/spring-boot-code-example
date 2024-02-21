package io.ngocnhan_tran1996.code.example.database.support.oracle.in;

import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.OracleTypeUtils;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import oracle.jdbc.driver.OracleConnection;

@Getter
class StructArrayTypeValue<T> extends ArrayTypeValue<T> {

    /**
     * The type name of the STRUCT
     **/
    private final String structTypeName;

    StructArrayTypeValue(String arrayTypeName, List<T> values, String structTypeName) {
        super(arrayTypeName, values);
        this.structTypeName = OracleTypeUtils.throwIfBlank(structTypeName);
    }

    @Override
    protected Object createTypeValue(Connection connection, String typeName) throws SQLException {

        List<Struct> structs = new ArrayList<>(super.getValues().size());

        for (var value : super.getValues()) {

            var struct = this.getOracleMapper()
                .toStruct(connection, value, this.getStructTypeName());
            structs.add(struct);
        }

        return connection
            .unwrap(OracleConnection.class)
            .createOracleArray(typeName, structs.toArray(Struct[]::new));
    }

    protected String getStructTypeName() {

        return Strings.toUpperCase(this.structTypeName);
    }

}