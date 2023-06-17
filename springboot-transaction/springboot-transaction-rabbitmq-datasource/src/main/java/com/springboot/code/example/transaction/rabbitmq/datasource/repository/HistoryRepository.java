package com.springboot.code.example.transaction.rabbitmq.datasource.repository;

import org.springframework.data.repository.CrudRepository;
import com.springboot.code.example.transaction.rabbitmq.datasource.entity.HistoryEntity;

public interface HistoryRepository extends CrudRepository<HistoryEntity, Integer> {

}