package com.springboot.entitymanager.service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.springboot.entitymanager.vehicle.VehicleEntityManagerService;
import com.springboot.entitymanager.wild.WildEntityManagerService;

@ExtendWith(MockitoExtension.class)
class MultipleEntityManagerWithAnnotationServiceTest {

  @Mock
  VehicleEntityManagerService vehicleEntityManagerService;

  @Mock
  WildEntityManagerService wildEntityManagerService;

  @Spy
  @InjectMocks
  MultipleEntityManagerWithAnnotationService multipleEntityManagerWithAnnotationService;

  @Test
  void testFindAll() {

    doNothing()
        .when(vehicleEntityManagerService)
        .execute();

    doNothing()
        .when(wildEntityManagerService)
        .execute();

    multipleEntityManagerWithAnnotationService.findAll();
    verify(multipleEntityManagerWithAnnotationService, atLeastOnce()).findAll();
  }

}