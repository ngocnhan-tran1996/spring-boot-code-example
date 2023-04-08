package com.springboot.code.example.database.profiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile("multiple-datasource")
@ComponentScan(basePackages = "com.springboot.code.example.database.multiple.datasource")
public @interface MultipleDatasource {

}
