package com.springboot.entitymanager.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import com.springboot.entitymanager.vehicle.VehicleEntityManagerService;
import com.springboot.entitymanager.wild.WildEntityManagerService;
import lombok.RequiredArgsConstructor;

@Profile("multiple")
@Service
@RequiredArgsConstructor
public class MultipleEntityManagerWithAnnotationService implements EntityManagerService {

  private final VehicleEntityManagerService vehicleEntityManagerService;
  private final WildEntityManagerService wildEntityManagerService;

  @Override
  public void findAll() {
    vehicleEntityManagerService.execute();
    wildEntityManagerService.execute();
  }

}