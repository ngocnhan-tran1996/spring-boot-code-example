package com.springboot.code.example.database.function;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import com.springboot.code.example.database.domain.name.NamePrefixRepository;
import com.springboot.code.example.database.function.testcase.NameInfoTestArguments.NameInfoInput;
import com.springboot.code.example.testcase.TestCase;

@ActiveProfiles("benchmark")
@SpringBootTest
class NameInfoTest {

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

  @TestCase("com.springboot.code.example.database.function.testcase.NameInfoTestArguments#testExecuteNameInfoArguments")
  void testExecuteNameInfoLoop(NameInfoInput input, int output) {

    var actualOutput = namePrefixRepository.executeNameInfoLoop(
        input.startDate(),
        input.toDate(),
        PageRequest.of(input.page(), input.size()));
    assertThat(actualOutput.getSize()).isEqualTo(output);
  }

  @TestCase("com.springboot.code.example.database.function.testcase.NameInfoTestArguments#testExecuteNameInfoArguments")
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

}