package io.ngocnhan_tran1996.code.example.rabbitmq.validation;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Employee {

    @NotBlank
    private String name;

}