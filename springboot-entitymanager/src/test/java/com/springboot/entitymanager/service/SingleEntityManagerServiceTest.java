package com.springboot.entitymanager.service;

import static org.mockito.Mockito.verify;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("single")
class SingleEntityManagerServiceTest {

  @Spy
  EntityManager entityManager;
  @Spy
  ModelMapper modelMapper;

  @Spy
  @InjectMocks
  SingleEntityManagerService singleEntityManagerService;

  @Test
  void findAll() {

    singleEntityManagerService.findAll();
    verify(singleEntityManagerService, atLea).findAll();

  }

}