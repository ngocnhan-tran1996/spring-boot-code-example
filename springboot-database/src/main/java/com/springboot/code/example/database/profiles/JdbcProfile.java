package com.springboot.code.example.database.profiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EntityScan
@EnableJpaRepositories
@SpringBootApplication
public @interface JdbcProfile {

  @AliasFor(annotation = SpringBootApplication.class)
  String[] scanBasePackages() default {};

  @AliasFor(annotation = EnableJpaRepositories.class)
  String[] basePackages() default {};

  @AliasFor(annotation = EntityScan.class, value = "basePackages")
  String[] packages() default {};
}
