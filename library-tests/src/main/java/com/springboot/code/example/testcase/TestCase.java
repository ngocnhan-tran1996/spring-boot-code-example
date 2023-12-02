package com.springboot.code.example.testcase;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.ArgumentsSource;

@Documented
@Target(ElementType.METHOD)
@TestTemplate
@ExtendWith(TestCaseExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(TestCaseProvider.class)
public @interface TestCase {

}