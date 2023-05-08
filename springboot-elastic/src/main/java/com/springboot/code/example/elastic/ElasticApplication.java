package com.springboot.code.example.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import co.elastic.apm.attach.ElasticApmAttacher;

@SpringBootApplication
public class ElasticApplication {

  public static void main(String[] args) {

    ElasticApmAttacher.attach();
    SpringApplication.run(ElasticApplication.class, args);
  }

}