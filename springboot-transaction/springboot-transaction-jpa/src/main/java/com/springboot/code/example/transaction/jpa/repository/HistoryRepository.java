package com.springboot.code.example.transaction.jpa.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.springboot.code.example.transaction.jpa.constant.QueryStrings;
import com.springboot.code.example.transaction.jpa.entity.HistoryEntity;

public interface HistoryRepository extends CrudRepository<HistoryEntity, Integer> {

  @Query(nativeQuery = true, value = QueryStrings.INSERT_STATEMENT)
  int insertHistory(String name);

  @Modifying
  @Query(nativeQuery = true, value = QueryStrings.INSERT_STATEMENT)
  int insertHistoryModifying(String name);

}