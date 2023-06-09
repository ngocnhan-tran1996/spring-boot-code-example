package com.springboot.code.example.database.jdbc.function;

import java.sql.SQLException;

@FunctionalInterface
public interface PropertyHandlerFunction<T, U> {

  void accept(T t, U u) throws SQLException;

}