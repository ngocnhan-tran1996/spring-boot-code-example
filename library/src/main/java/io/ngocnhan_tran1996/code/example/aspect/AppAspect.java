package io.ngocnhan_tran1996.code.example.aspect;

import io.ngocnhan_tran1996.code.example.trace.ThrowableTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
@RequiredArgsConstructor
public class AppAspect {

    private final ThrowableTrace throwableTrace;

    @AfterThrowing(pointcut = "execution(* io.ngocnhan_tran1996.code.example..*.*(..))",
        throwing = "ex")
    void afterThrowingAdvice(Exception ex) throws Throwable {

        var error = ThrowableTraceError.get(ex)
            .apply(throwableTrace);

        log.error("{}", error.toString());
        throw error.getThrowable();
    }

}