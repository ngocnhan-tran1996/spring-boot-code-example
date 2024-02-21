package io.ngocnhan_tran1996.code.example.database.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import io.ngocnhan_tran1996.code.example.database.dto.NamePrefixInput;
import io.ngocnhan_tran1996.code.example.database.dto.NamePrefixRecordInput;
import io.ngocnhan_tran1996.code.example.database.support.oracle.in.OracleTypeValue;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedCaseInsensitiveMap;

@ActiveProfiles("entity-manager")
@SpringBootTest
class JdbcServiceTest {

    @Autowired
    JdbcService jdbcService;

    @Test
    void testExcuteConcatNameProc() {

        var expectOutput = Map.of(
            "IN_NAME", "Nhan",
            "OUT_MSG", "IN_NAME = Nhan AND IN_PARAMS = NAME: Nhan Ngoc AND NAME: Ngoc Nhan");

        var classData = OracleTypeValue.withArray(NamePrefixInput.class,
                "jdbc_example_pack.name_array")
            .withTypeName("jdbc_example_pack.name_record")
            .value(new NamePrefixInput("Nhan", "Ngoc"))
            .value(new NamePrefixInput("Ngoc", "Nhan"))
            .toTypeValue();

        assertThat(jdbcService.excuteConcatNameProc(classData))
            .isEqualTo(expectOutput);

        var recordData = OracleTypeValue.withArray(NamePrefixRecordInput.class,
                "jdbc_example_pack.name_array")
            .withTypeName("jdbc_example_pack.name_record")
            .value(new NamePrefixRecordInput("Nhan", "Ngoc"))
            .value(new NamePrefixRecordInput("Ngoc", "Nhan"))
            .toTypeValue();

        assertThat(jdbcService.excuteConcatNameProc(recordData))
            .isEqualTo(expectOutput);
    }

    @Test
    void testExecuteComplexTypeOutProc() {

        var expectOutput = new LinkedCaseInsensitiveMap<Object>();
        expectOutput.put(
            "out_names", new NamePrefixRecordInput[]{
                new NamePrefixRecordInput("first_name 0", "last_name 0"),
                new NamePrefixRecordInput("first_name 1", "last_name 1"),
                new NamePrefixRecordInput("first_name 2", "last_name 2"),
                new NamePrefixRecordInput("first_name 3", "last_name 3"),
                new NamePrefixRecordInput("first_name 4", "last_name 4"),
                new NamePrefixRecordInput("first_name 5", "last_name 5")});
        expectOutput.put("out_numbers", new BigDecimal[]{
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
            "out_names", new NamePrefixInput[]{
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

    @Test
    void testExecutePlusOneFunc() {

        var expectOutput = Map.of(
            "OUT_MSG", "Not negative number",
            "OUT_NBR", BigDecimal.ONE,
            "return", BigDecimal.valueOf(2));

        assertThat(jdbcService.executePlusOneFunc())
            .isEqualTo(expectOutput);
    }

    @Test
    void testExecuteNameInfoTableFunc() {

        assertThatExceptionOfType(BadSqlGrammarException.class)
            .isThrownBy(jdbcService::executeNameInfoTableFunc);
    }

    @Test
    void testExecuteInOutObject() {

        var input = new NamePrefixInput("Nhan", "Ngoc");
        var result = jdbcService.executeInOutObject(NamePrefixInput.class, input, false);
        var output = new NamePrefixInput("Ngoc0", "Nhan1");
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(output);

        // record class
        var recordInput = new NamePrefixRecordInput("EBfTRJOyiKhZklCONAaA", "tshbpckypydpxztdrvkn");
        var recordResult = jdbcService.executeInOutObject(NamePrefixRecordInput.class, recordInput,
            false);
        assertThat(recordResult)
            .usingRecursiveComparison()
            .isEqualTo(new NamePrefixRecordInput("tshbpckypydpxztdrvkn0", "EBfTRJOyiKhZklCONAaA1"));

        // null obj
        result = jdbcService.executeInOutObject(NamePrefixInput.class, input, true);
        output = new NamePrefixInput("NGOC", "NHAN");
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(output);
    }

}