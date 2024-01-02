package io.ngocnhan_tran1996.code.example.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.ngocnhan_tran1996.code.example.logger")
public class Log4j2Application {

  public static void main(String[] args) {
    SpringApplication.run(Log4j2Application.class, args);
  }

}