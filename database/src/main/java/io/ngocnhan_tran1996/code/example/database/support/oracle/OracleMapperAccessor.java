package io.ngocnhan_tran1996.code.example.database.support.oracle;

import io.ngocnhan_tran1996.code.example.database.support.oracle.mapper.DelegateOracleMapper;
import io.ngocnhan_tran1996.code.example.database.support.oracle.mapper.OracleMapper;

public interface OracleMapperAccessor<S extends OracleMapperAccessor<S>> extends Self<S> {

  DelegateOracleMapper delegate = new DelegateOracleMapper();

  default <T> OracleMapper<T> getOracleMapper() {

    return delegate.getOracleMapper();
  }

  default <T> OracleMapper<T> getOracleMapper(Class<T> mappedClass) {

    return delegate.getOracleMapper(mappedClass);
  }

  default <T> S setOracleMapper(OracleMapper<T> oracleMapper) {

    delegate.setOracleMapper(oracleMapper);
    return self();
  }

}