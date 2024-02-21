package io.ngocnhan_tran1996.code.example.exception;

public class ConditionViolationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConditionViolationException(String message) {
        super(message);
    }

}