package io.ngocnhan_tran1996.code.example.aspect;

import io.ngocnhan_tran1996.code.example.trace.ThrowableTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppAspectConfig {

    @Bean
    ThrowableTrace throwableTrace() {

        return new ThrowableTrace();
    }

}