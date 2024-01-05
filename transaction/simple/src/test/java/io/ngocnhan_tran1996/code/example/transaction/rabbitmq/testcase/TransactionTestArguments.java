package io.ngocnhan_tran1996.code.example.transaction.rabbitmq.testcase;

import io.ngocnhan_tran1996.code.example.testcase.TestArguments;

class TransactionTestArguments {

  static TestArguments testSendArguments = TestArguments
      .params(true, 0)
      .nextParams(false, 1);

  static TestArguments testSendWithCloneArguments = TestArguments
      .params(true, 1)
      .nextParams(false, 1);

}