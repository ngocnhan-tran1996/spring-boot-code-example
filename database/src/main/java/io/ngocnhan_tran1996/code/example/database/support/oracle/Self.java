package io.ngocnhan_tran1996.code.example.database.support.oracle;

public interface Self<T> {

    @SuppressWarnings("unchecked")
    default T self() {

        return (T) this;
    }

}