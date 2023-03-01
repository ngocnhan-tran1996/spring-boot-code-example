package com.springboot.multipledatasource.service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.springboot.multipledatasource.vehicle.CardRepository;
import com.springboot.multipledatasource.wild.AnimalRepository;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {

  @Mock
  CardRepository cardRepository;
  @Mock
  AnimalRepository animalRepository;

  @Spy
  @InjectMocks
  DataService dataService;

  @Test
  void testManipulate() {

    dataService.manipulate();
    verify(dataService, atLeastOnce()).manipulate();
  }

}