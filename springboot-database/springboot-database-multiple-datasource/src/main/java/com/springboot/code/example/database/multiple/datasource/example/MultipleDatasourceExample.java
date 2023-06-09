package com.springboot.code.example.database.multiple.datasource.example;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.database.multiple.datasource.entity.BaseEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CarEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CarRepository;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalEntity;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MultipleDatasourceExample {

  private final CarRepository carRepository;
  private final AnimalRepository animalRepository;

  @Transactional(transactionManager = "platformTransactionManager")
  public List<BaseEntity> getAllBaseEntity() {

    var newCar = new CarEntity();
    newCar.setName("BENZ");
    carRepository.save(newCar);
    carRepository.deleteById(1);

    var newAnimal = new AnimalEntity();
    newAnimal.setName("DOG");
    animalRepository.save(newAnimal);
    animalRepository.deleteById(1);

    List<BaseEntity> baseEntities = new ArrayList<>();
    carRepository.findAll().forEach(baseEntities::add);
    animalRepository.findAll().forEach(baseEntities::add);
    return baseEntities;
  }

}