package com.springboot.code.example.database.multiple.datasource;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.springboot.code.example.database.multiple.datasource.annotation.MultipleDatasourceConfiguration;
import com.springboot.code.example.database.multiple.datasource.vehicle.CardRepository;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalRepository;

@MultipleDatasourceConfiguration
@ExtendWith(MockitoExtension.class)
class DatasourceExecutorTest {

  @Mock
  CardRepository cardRepository;

  @Mock
  AnimalRepository animalRepository;

  @Spy
  @InjectMocks
  DatasourceExecutor datasourceExecutor;

  @Test
  void testManipulate() {

    datasourceExecutor.manipulate();
    verify(datasourceExecutor, atLeastOnce()).manipulate();
  }

}