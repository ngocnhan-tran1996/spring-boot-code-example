package com.springboot.code.example.database.problem.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Aspect
// @Component
public class ExecutionTimer {

  @Around("execution(* com.springboot.code.example.database.problem.repository.*.*(..))")
  Object doExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

    var signature = proceedingJoinPoint.getSignature();
    String className = signature.getDeclaringType().getSimpleName();
    String methodName = signature.getName();

    final var stopWatch = new StopWatch();
    stopWatch.start();
    Object retVal = proceedingJoinPoint.proceed();
    stopWatch.stop();

    log.info("Execution time of {}.{} : {} ns",
        className,
        methodName,
        stopWatch.getTotalTimeNanos());

    return retVal;
  }

}