package io.ngocnhan_tran1996.code.example.database.support.oracle;

import io.ngocnhan_tran1996.code.example.database.support.oracle.mapper.DelegateOracleMapper;
import io.ngocnhan_tran1996.code.example.database.support.oracle.mapper.OracleMapper;

public interface OracleMapperAccessor<S extends OracleMapperAccessor<S>> extends Self<S> {

  default <T> OracleMapper<T> getOracleMapper() {

    return this.getDelegateOracleMapper().getOracleMapper();
  }

  default <T> OracleMapper<T> getOracleMapper(Class<T> mappedClass) {

    return this.getDelegateOracleMapper().getOracleMapper(mappedClass);
  }

  default <T> S setOracleMapper(OracleMapper<T> oracleMapper) {

    this.getDelegateOracleMapper().setOracleMapper(oracleMapper);
    return self();
  }

  DelegateOracleMapper getDelegateOracleMapper();

}