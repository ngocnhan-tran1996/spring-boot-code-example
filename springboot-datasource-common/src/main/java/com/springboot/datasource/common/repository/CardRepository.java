package com.springboot.datasource.common.repository;

import org.springframework.data.repository.CrudRepository;

import com.springboot.datasource.common.entity.CarEntity;

public interface CardRepository extends CrudRepository<CarEntity, Integer> {

}