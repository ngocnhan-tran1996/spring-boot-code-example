package com.springboot.code.example.database.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedCaseInsensitiveMap;
import com.springboot.code.example.database.dto.NamePrefixInput;
import com.springboot.code.example.database.dto.NamePrefixRecordInput;

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
  void testExecuteComplexTypeOutProc() {

    var expectOutput = new LinkedCaseInsensitiveMap<Object>();
    expectOutput.put(
        "out_names", new NamePrefixRecordInput[] {
            new NamePrefixRecordInput("first_name 0", "last_name 0"),
            new NamePrefixRecordInput("first_name 1", "last_name 1"),
            new NamePrefixRecordInput("first_name 2", "last_name 2"),
            new NamePrefixRecordInput("first_name 3", "last_name 3"),
            new NamePrefixRecordInput("first_name 4", "last_name 4"),
            new NamePrefixRecordInput("first_name 5", "last_name 5")});
    expectOutput.put("out_numbers", new BigDecimal[] {
        BigDecimal.ZERO,
        BigDecimal.ONE,
        BigDecimal.valueOf(2),
        BigDecimal.valueOf(3),
        BigDecimal.valueOf(4),
        BigDecimal.valueOf(5)});

    assertThat(jdbcService.executeComplexTypeOutProc(NamePrefixRecordInput.class))
        .usingRecursiveComparison()
        .isEqualTo(expectOutput);

    expectOutput.put(
        "out_names", new NamePrefixInput[] {
            new NamePrefixInput("first_name 0", "last_name 0"),
            new NamePrefixInput("first_name 1", "last_name 1"),
            new NamePrefixInput("first_name 2", "last_name 2"),
            new NamePrefixInput("first_name 3", "last_name 3"),
            new NamePrefixInput("first_name 4", "last_name 4"),
            new NamePrefixInput("first_name 5", "last_name 5")});

    assertThat(jdbcService.executeComplexTypeOutProc(NamePrefixInput.class))
        .usingRecursiveComparison()
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