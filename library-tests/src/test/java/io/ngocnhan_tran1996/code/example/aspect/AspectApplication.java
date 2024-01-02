package io.ngocnhan_tran1996.code.example.aspect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.ngocnhan_tran1996.code.example.aspect")
public class AspectApplication {

  public static void main(String[] args) {
    SpringApplication.run(AspectApplication.class, args);
  }

}