package io.ngocnhan_tran1996.code.example.container;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration
public @interface EnableTestcontainers {

  @AliasFor(annotation = ContextConfiguration.class, attribute = "initializers")
  Class<? extends ApplicationContextInitializer<?>>[] value() default {};

}