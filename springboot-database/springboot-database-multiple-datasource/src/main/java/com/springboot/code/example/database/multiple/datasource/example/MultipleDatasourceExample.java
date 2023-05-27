package com.springboot.code.example.database.multiple.datasource.example;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.springboot.code.example.database.multiple.datasource.entity.BaseEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CarEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CardRepository;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalEntity;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MultipleDatasourceExample {

  private final CardRepository cardRepository;
  private final AnimalRepository animalRepository;

  public List<BaseEntity> execute() {

    var newCar = new CarEntity();
    newCar.setName("BENZ");
    cardRepository.save(newCar);

    var newAnimal = new AnimalEntity();
    newAnimal.setName("DOG");
    animalRepository.save(newAnimal);

    animalRepository.deleteById(1);
    cardRepository.deleteById(1);

    List<BaseEntity> baseEntities = new ArrayList<>();
    cardRepository.findAll().forEach(baseEntities::add);
    animalRepository.findAll().forEach(baseEntities::add);
    return baseEntities;
  }

}