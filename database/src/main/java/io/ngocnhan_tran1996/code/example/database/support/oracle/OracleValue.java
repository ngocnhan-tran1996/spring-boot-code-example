package io.ngocnhan_tran1996.code.example.database.support.oracle;

import io.ngocnhan_tran1996.code.example.database.support.oracle.mapper.DelegateOracleMapper;
import lombok.Getter;

public abstract class OracleValue<S extends OracleValue<S>> implements
    OracleMapperAccessor<S> {

  protected Class<?> clazz;
  @Getter
  private DelegateOracleMapper delegateOracleMapper = new DelegateOracleMapper();

  /** The type name of the STRUCT **/
  protected String typeName;

  public S withTypeName(String typeName) {

    this.typeName = typeName;
    return self();
  }

}