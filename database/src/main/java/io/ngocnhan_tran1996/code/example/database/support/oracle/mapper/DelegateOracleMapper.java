package io.ngocnhan_tran1996.code.example.database.support.oracle.mapper;

import java.util.function.Predicate;

public final class DelegateOracleMapper {

    private static final String RECORD_CLASS_NAME = "java.lang.Record";
    private static final Predicate<Class<?>> recordClassPredicate = mappedClass ->
        mappedClass != null
            && mappedClass.getSuperclass() != null
            && RECORD_CLASS_NAME.equals(mappedClass.getSuperclass().getName());

    private OracleMapper<?> oracleMapper;

    public static <T> OracleMapper<T> get(Class<T> mappedClass) {

        return getDefault(mappedClass);
    }

    private static <T> OracleMapper<T> getDefault(Class<T> mappedClass) {

        return recordClassPredicate.test(mappedClass)
            ? RecordPropertyMapper.newInstance(mappedClass)
            : BeanPropertyMapper.newInstance(mappedClass);
    }

    public <T> OracleMapper<T> getOracleMapper(Class<T> mappedClass) {

        return getDefault(mappedClass);
    }

    @SuppressWarnings("unchecked")
    public <T> OracleMapper<T> getOracleMapper() {

        return (OracleMapper<T>) this.oracleMapper;
    }

    public <T> void setOracleMapper(OracleMapper<T> oracleMapper) {

        if (oracleMapper != null) {
            this.oracleMapper = oracleMapper;
        }
    }
}