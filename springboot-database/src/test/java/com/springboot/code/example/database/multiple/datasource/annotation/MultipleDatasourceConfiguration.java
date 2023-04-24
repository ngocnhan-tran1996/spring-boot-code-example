package com.springboot.code.example.database.multiple.datasource.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import com.springboot.code.example.database.profiles.DatabaseProfiles;
import com.springboot.code.example.database.profiles.ProfilePackages;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles(DatabaseProfiles.MULTIPLE_DATASOURCE)
@ComponentScan(basePackages = ProfilePackages.MULTIPLE_DATASOURCE)
public @interface MultipleDatasourceConfiguration {

}
