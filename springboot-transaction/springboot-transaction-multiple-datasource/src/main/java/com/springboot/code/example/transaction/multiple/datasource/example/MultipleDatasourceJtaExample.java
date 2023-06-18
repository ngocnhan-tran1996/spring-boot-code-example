package com.springboot.code.example.transaction.multiple.datasource.example;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.transaction.multiple.datasource.entity.BaseEntity;
import com.springboot.code.example.transaction.multiple.datasource.jta.vehicle.CarJtaEntity;
import com.springboot.code.example.transaction.multiple.datasource.jta.vehicle.CarJtaRepository;
import com.springboot.code.example.transaction.multiple.datasource.jta.wild.AnimalJtaEntity;
import com.springboot.code.example.transaction.multiple.datasource.jta.wild.AnimalJtaRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(transactionManager = "transactionManager")
@RequiredArgsConstructor
public class MultipleDatasourceJtaExample {

  private final CarJtaRepository carJtaRepository;
  private final AnimalJtaRepository animalJtaRepository;

  public List<BaseEntity> getAll() {

    List<BaseEntity> baseEntities = new ArrayList<>();
    baseEntities.addAll(this.getCars());
    baseEntities.addAll(this.getAnimals());
    return baseEntities;
  }

  public List<BaseEntity> getAllWithException() {

    List<BaseEntity> baseEntities = new ArrayList<>();
    baseEntities.addAll(this.getCars());
    baseEntities.addAll(this.getAnimals());

    carJtaRepository.updateHistory();
    return baseEntities;
  }

  public List<BaseEntity> getAllWithInvalidDataAccessResourceUsageException() {

    List<BaseEntity> baseEntities = new ArrayList<>();
    baseEntities.addAll(this.getCars());
    baseEntities.addAll(this.getAnimals());

    carJtaRepository.updateHistory("test");

    return baseEntities;
  }

  public List<BaseEntity> getCars() {

    var newCar = new CarJtaEntity();
    newCar.setName("BENZ");
    carJtaRepository.save(newCar);
    carJtaRepository.deleteById(1);

    List<BaseEntity> baseEntities = new ArrayList<>();
    carJtaRepository.findAll().forEach(baseEntities::add);
    return baseEntities;
  }

  public List<BaseEntity> getCarsWithException() {

    List<BaseEntity> baseEntities = this.getCars();
    carJtaRepository.updateHistory("test");
    return baseEntities;
  }


  public List<BaseEntity> getAnimals() {

    var newAnimal = new AnimalJtaEntity();
    newAnimal.setName("DOG");
    animalJtaRepository.save(newAnimal);
    animalJtaRepository.deleteById(1);

    List<BaseEntity> baseEntities = new ArrayList<>();
    animalJtaRepository.findAll().forEach(baseEntities::add);
    return baseEntities;
  }

}