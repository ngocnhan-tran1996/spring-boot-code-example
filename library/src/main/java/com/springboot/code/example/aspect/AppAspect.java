package com.springboot.code.example.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.springboot.code.example.trace.ThrowableTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Aspect
@Component
@RequiredArgsConstructor
public class AppAspect {

  private final ThrowableTrace throwableTrace;

  @AfterThrowing(pointcut = "execution(* com.springboot.code.example..*.*(..))",
      throwing = "ex")
  void afterThrowingAdvice(Exception ex) throws Throwable {

    var error = ThrowableTraceError.get(ex)
        .apply(throwableTrace);

    log.error("{}", error.toString());
    throw error.getThrowable();
  }

}