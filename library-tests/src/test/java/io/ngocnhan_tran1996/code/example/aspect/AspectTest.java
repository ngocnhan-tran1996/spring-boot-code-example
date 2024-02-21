package io.ngocnhan_tran1996.code.example.aspect;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AspectTest {

    @Autowired
    AspectService aspectService;

    @TestCase
    <T extends Throwable> void testAspect(T throwable, Class<T> classEx) {

        assertThatExceptionOfType(classEx)
            .isThrownBy(() -> aspectService.throwRuntime(throwable));
    }

}