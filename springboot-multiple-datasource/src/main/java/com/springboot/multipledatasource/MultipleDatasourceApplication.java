package com.springboot.multipledatasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.springboot.multipledatasource.service.DataService;

@SpringBootApplication
public class MultipleDatasourceApplication implements CommandLineRunner {

  @Autowired
  private DataService dataService;

  public static void main(String[] args) {
    SpringApplication.run(MultipleDatasourceApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    dataService.manipulate();
  }

}