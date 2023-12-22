package com.springboot.code.example.rabbitmq.validation;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Employee {

  @NotBlank
  private String name;

}
