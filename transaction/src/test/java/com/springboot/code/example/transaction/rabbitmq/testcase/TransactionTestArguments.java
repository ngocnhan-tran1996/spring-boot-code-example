package com.springboot.code.example.transaction.rabbitmq.testcase;

import com.springboot.code.example.testcase.TestArguments;

class TransactionTestArguments {

  static TestArguments testSendArguments = TestArguments
      .params(true, 0)
      .nextParams(false, 1);

  static TestArguments testSendWithCloneArguments = TestArguments
      .params(true, 1)
      .nextParams(false, 1);

}