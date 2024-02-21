package io.ngocnhan_tran1996.code.example.database.support.oracle.exception;

import java.io.Serial;

public class OracleTypeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public OracleTypeException(String msg) {
        super(msg);
    }

}