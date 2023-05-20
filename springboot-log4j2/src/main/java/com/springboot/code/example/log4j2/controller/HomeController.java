package com.springboot.code.example.log4j2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.code.example.common.helper.Strings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
public class HomeController {

  @GetMapping
  public Test getHome() {

    var test = new Test();
    log.info("Response: {}", Strings.toString(test));
    return new Test();
  }

  @Getter
  @Setter
  public static class Test {

    private String key = "Password";
    private String anotherTest = "Test";
  }

}
