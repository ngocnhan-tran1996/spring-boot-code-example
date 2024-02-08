package io.ngocnhan_tran1996.code.example.database.support.oracle;

import org.springframework.beans.BeanUtils;
import io.ngocnhan_tran1996.code.example.database.support.oracle.mapper.DelegateOracleMapper;
import io.ngocnhan_tran1996.code.example.database.support.oracle.out.AbstractSqlReturnType;
import io.ngocnhan_tran1996.code.example.database.support.oracle.out.ArrayReturnType;
import io.ngocnhan_tran1996.code.example.database.support.oracle.out.StructArrayReturnType;
import io.ngocnhan_tran1996.code.example.database.support.oracle.out.StructReturnType;
import lombok.Getter;

public abstract class OracleValue<S extends OracleValue<S>> implements OracleMapperAccessor<S> {

  protected Class<?> clazz;
  @Getter
  private DelegateOracleMapper delegateOracleMapper = new DelegateOracleMapper();
  protected String parameterName;

  /** The type name of the STRUCT **/
  protected String typeName;

  public S withTypeName(String typeName) {

    this.typeName = typeName;
    return self();
  }

  protected <T> AbstractSqlReturnType<T> returnType() {

    if (this.clazz == null
        || BeanUtils.isSimpleValueType(this.clazz)) {

      return new ArrayReturnType<>();
    }

    return this.isStructType()
        ? new StructReturnType<T>()
            .setOracleMapper(this.getOracleMapper(this.clazz))
        : new StructArrayReturnType<T>()
            .setOracleMapper(this.getOracleMapper(this.clazz));
  }

  protected abstract boolean isStructType();

}