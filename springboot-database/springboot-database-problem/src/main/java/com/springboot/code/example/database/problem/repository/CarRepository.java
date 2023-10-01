package com.springboot.code.example.database.problem.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.springboot.code.example.database.problem.entity.CarEntity;

public interface CarRepository extends JpaRepository<CarEntity, BigDecimal> {

  @Query(value = "SELECT c FROM CarEntity c WHERE c.id = :id OR :id IS NULL")
  List<CarEntity> bindNullParameter(BigDecimal id);

  @Query(value = "SELECT c FROM CarEntity c WHERE c.id = :id")
  List<CarEntity> bindParameter(BigDecimal id);

  @Query(value = "SELECT c.* FROM car c WHERE c.id = :id OR :id IS NULL", nativeQuery = true)
  List<CarEntity> nativeBindNullParameter(BigDecimal id);

  @Query(value = "SELECT c.* FROM car c WHERE c.id = :id", nativeQuery = true)
  List<CarEntity> nativeBindParameter(BigDecimal id);

}