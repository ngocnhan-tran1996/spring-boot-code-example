package com.springboot.code.example.rabbitmq.transaction.testcase;

import java.util.List;
import com.springboot.code.example.testcase.TestArguments;

class TransactionTestArguments {

  static List<TestArguments<Boolean, Integer>> testSendArguments = List.of(
      TestArguments.of(true, 0),
      TestArguments.of(false, 1));

}