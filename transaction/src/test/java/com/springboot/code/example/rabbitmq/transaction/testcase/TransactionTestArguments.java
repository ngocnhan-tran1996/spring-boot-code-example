package com.springboot.code.example.rabbitmq.transaction.testcase;

import com.springboot.code.example.testcase.TestArguments;

class TransactionTestArguments {

  static TestArguments testSendArguments = TestArguments
      .params(true, 0)
      .nextParams(false, 1);

}