package io.ngocnhan_tran1996.code.example.https;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@TestConfiguration
@RestController
public class HomeController {

  @PostMapping("/")
  Person post(@RequestBody Person person) {

    return person;
  }

  public record Person(String name, Integer age) {

  }

}