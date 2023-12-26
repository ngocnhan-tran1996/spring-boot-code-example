package com.springboot.code.example.database.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.springboot.code.example.database.domain.name.NamePrefixRecordInput;

@ActiveProfiles("entity-manager")
@SpringBootTest
class JdbcServiceTest {

  @Autowired
  JdbcService jdbcService;

  @Test
  void testExcuteConcatNameProc() {

    var expectOutput = Map.of(
        "IN_NAME", "Nhan",
        "OUT_MSG", "IN_NAME = Nhan AND IN_PARAMS =  NAME: Nhan Ngoc AND  NAME: Ngoc Nhan");
    assertThat(jdbcService.excuteConcatNameProc())
        .isEqualTo(expectOutput);
  }

  @Test
  void testExecuteNameInfoFunc() {

    var expectOutput = List.of(
        new NamePrefixRecordInput("EBfTRJOyiKhZklCONAaA", "tshbpckypydpxztdrvkn"),
        new NamePrefixRecordInput("lyeTZIvhfzVorssiGAPw", "dkopekqjolvzzcjgwfsf"),
        new NamePrefixRecordInput("EqkWpzOCzCEoNxGvSMlD", "qtqpcuasbowfvzjykvqe"),
        new NamePrefixRecordInput("WmHrVwuRSEQZxQoIelHs", "odgvbmsrutcuzwxarnxl"),
        new NamePrefixRecordInput("tQJAshUVkapxyuzYKsaK", "rzdizfnpvspmbiwkuqft"),
        new NamePrefixRecordInput("yagohEOIlVMSaDnrcjgk", "izwtvrjtnffejdzibkms"),
        new NamePrefixRecordInput("gmyRnSPeAaFGhFZpJzcI", "hnicofromgnelpacjeiv"),
        new NamePrefixRecordInput("JXuIRlycFbZSwroIxhuM", "pvtlcdgzhvqjlliqfiqr"),
        new NamePrefixRecordInput("mJvVEQRUMwSyAEOOfHIf", "oqxozvzeelvsvvqsdcvi"));
    assertThat(jdbcService.executeNameInfoFunc())
        .isEqualTo(expectOutput);
  }

}