package io.ngocnhan_tran1996.code.example.database.benchmark;

import io.ngocnhan_tran1996.code.example.database.DatabaseApplication;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

abstract class BenchmarkConfig {

    volatile ConfigurableApplicationContext applicationContext;

    @Setup
    public void init() {

        var application = new SpringApplication(DatabaseApplication.class);
        application.setAdditionalProfiles("benchmark");
        this.applicationContext = application.run();
        var factory = this.applicationContext.getAutowireCapableBeanFactory();
        factory.autowireBean(this);
    }

    @TearDown
    public void clean() {

        this.applicationContext.close();
    }

}