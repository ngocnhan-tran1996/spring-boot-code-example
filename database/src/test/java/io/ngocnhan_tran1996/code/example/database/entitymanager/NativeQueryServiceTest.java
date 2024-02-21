package io.ngocnhan_tran1996.code.example.database.entitymanager;

import static org.assertj.core.api.Assertions.assertThat;

import io.ngocnhan_tran1996.code.example.database.domain.NamePrefixResponse;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("entity-manager")
@SpringBootTest
class NativeQueryServiceTest {

    @Autowired
    NativeQueryService nativeQueryService;

    @Test
    void testNativeQueryNamePrefix() {

        assertThat(nativeQueryService.nativeQueryNamePrefix())
            .hasSize(9)
            .contains(
                new NamePrefixResponse(
                    "EBfTRJOyiKhZklCONAaA",
                    "tshbpckypydpxztdrvkn",
                    BigDecimal.ONE));
    }

}