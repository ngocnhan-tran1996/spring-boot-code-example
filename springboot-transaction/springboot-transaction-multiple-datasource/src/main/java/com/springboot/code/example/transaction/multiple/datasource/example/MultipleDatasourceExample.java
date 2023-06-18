package com.springboot.code.example.transaction.multiple.datasource.example;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.transaction.multiple.datasource.entity.BaseEntity;
import com.springboot.code.example.transaction.multiple.datasource.vehicle.CarEntity;
import com.springboot.code.example.transaction.multiple.datasource.vehicle.CarRepository;
import com.springboot.code.example.transaction.multiple.datasource.wild.AnimalEntity;
import com.springboot.code.example.transaction.multiple.datasource.wild.AnimalRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MultipleDatasourceExample {

  private final CarRepository carRepository;
  private final AnimalRepository animalRepository;

  @Transactional(value = "chainedTransactionManager")
  public List<BaseEntity> getAllWithException() {

    List<BaseEntity> baseEntities = new ArrayList<>();
    baseEntities.addAll(this.getCars());
    baseEntities.addAll(this.getAnimals());

    carRepository.updateHistory();

    return baseEntities;
  }

  @Transactional(value = "chainedTransactionManager")
  public List<BaseEntity> getAllWithInvalidDataAccessResourceUsageException() {

    List<BaseEntity> baseEntities = new ArrayList<>();
    baseEntities.addAll(this.getCars());
    baseEntities.addAll(this.getAnimals());

    carRepository.updateHistory("test");

    return baseEntities;
  }

  @Transactional(value = "chainedTransactionManager")
  public List<BaseEntity> getAll() {

    List<BaseEntity> baseEntities = new ArrayList<>();
    baseEntities.addAll(this.getCars());
    baseEntities.addAll(this.getAnimals());
    return baseEntities;
  }

  @Transactional(value = "vehicleTransactionManager")
  public List<BaseEntity> getCars() {

    var newCar = new CarEntity();
    newCar.setName("BENZ");
    carRepository.save(newCar);
    carRepository.deleteById(1);

    List<BaseEntity> baseEntities = new ArrayList<>();
    carRepository.findAll().forEach(baseEntities::add);
    return baseEntities;
  }

  @Transactional(value = "vehicleTransactionManager")
  public List<BaseEntity> getCarsWithException() {

    List<BaseEntity> baseEntities = this.getCars();
    carRepository.updateHistory("test");
    return baseEntities;
  }

  @Transactional(transactionManager = "wildTransactionManager")
  public List<BaseEntity> getAnimals() {

    var newAnimal = new AnimalEntity();
    newAnimal.setName("DOG");
    animalRepository.save(newAnimal);
    animalRepository.deleteById(1);

    List<BaseEntity> baseEntities = new ArrayList<>();
    animalRepository.findAll().forEach(baseEntities::add);
    return baseEntities;
  }

}