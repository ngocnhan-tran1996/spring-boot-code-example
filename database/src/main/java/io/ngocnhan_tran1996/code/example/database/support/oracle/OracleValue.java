package io.ngocnhan_tran1996.code.example.database.support.oracle;

public abstract class OracleValue<T, S extends OracleValue<T, S>> implements
    OracleMapperAccessor<S> {

  protected Class<T> clazz;

  /** The type name of the STRUCT **/
  protected String typeName;

  public S withTypeName(String typeName) {

    this.typeName = typeName;
    return self();
  }

}