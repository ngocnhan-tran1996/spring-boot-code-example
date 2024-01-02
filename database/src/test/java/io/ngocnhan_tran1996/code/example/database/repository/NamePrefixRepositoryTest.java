package io.ngocnhan_tran1996.code.example.database.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import io.ngocnhan_tran1996.code.example.database.domain.NamePrefixRepository;
import io.ngocnhan_tran1996.code.example.database.repository.testcase.NamePrefixRepositoryTestArguments.NameInfoInput;

@ActiveProfiles("benchmark")
@SpringBootTest
class NamePrefixRepositoryTest {

  @Autowired
  NamePrefixRepository namePrefixRepository;

  @TestCase
  void testExecuteNameInfo(NameInfoInput input, int output) {

    var actualOutput = namePrefixRepository.executeNameInfo(
        input.startDate(),
        input.toDate(),
        PageRequest.of(input.page(), input.size()));
    assertThat(actualOutput.getSize()).isEqualTo(output);
  }

  @TestCase("io.ngocnhan_tran1996.code.example.database.repository.testcase.NamePrefixRepositoryTestArguments#testExecuteNameInfoArguments")
  void testExecuteNameInfoLoop(NameInfoInput input, int output) {

    var actualOutput = namePrefixRepository.executeNameInfoLoop(
        input.startDate(),
        input.toDate(),
        PageRequest.of(input.page(), input.size()));
    assertThat(actualOutput.getSize()).isEqualTo(output);
  }

  @TestCase("io.ngocnhan_tran1996.code.example.database.repository.testcase.NamePrefixRepositoryTestArguments#testExecuteNameInfoArguments")
  void testPaginateNameInfo(NameInfoInput input, int output) {

    int fromIndex = input.page() * input.size();
    int toIndex = fromIndex + input.size();
    var actualOutput = namePrefixRepository.paginateNameInfo(
        input.startDate(),
        input.toDate(),
        fromIndex,
        toIndex);
    assertThat(actualOutput.size()).isEqualTo(output);
  }

  @TestCase("io.ngocnhan_tran1996.code.example.database.repository.testcase.NamePrefixRepositoryTestArguments#testExecuteNameInfoArguments")
  void testPaginateNameInfoLoop(NameInfoInput input, int output) {

    int fromIndex = input.page() * input.size();
    int toIndex = fromIndex + input.size();
    var actualOutput = namePrefixRepository.paginateNameInfoLoop(
        input.startDate(),
        input.toDate(),
        fromIndex,
        toIndex);
    assertThat(actualOutput.size()).isEqualTo(output);
  }

  @TestCase("io.ngocnhan_tran1996.code.example.database.repository.testcase.NamePrefixRepositoryTestArguments#testBindParameterArguments")
  void testBindNullParameter(BigDecimal input, Integer output) {

    var actualOutput = namePrefixRepository.bindNullParameter(input)
        .size();
    assertThat(actualOutput).isEqualTo(output);
  }

  @TestCase
  void testBindParameter(BigDecimal input, Integer output) {

    var actualOutput = namePrefixRepository.bindParameter(input)
        .size();
    assertThat(actualOutput).isEqualTo(output);
  }

  @TestCase("io.ngocnhan_tran1996.code.example.database.repository.testcase.NamePrefixRepositoryTestArguments#testBindParameterArguments")
  void testBindNullParameterNative(BigDecimal input, Integer output) {

    var actualOutput = namePrefixRepository.bindNullParameterNative(input)
        .size();
    assertThat(actualOutput).isEqualTo(output);
  }

  @TestCase("io.ngocnhan_tran1996.code.example.database.repository.testcase.NamePrefixRepositoryTestArguments#testBindParameterArguments")
  void testBindParameterNative(BigDecimal input, Integer output) {

    var actualOutput = namePrefixRepository.bindParameterNative(input)
        .size();
    assertThat(actualOutput).isEqualTo(output);
  }

}