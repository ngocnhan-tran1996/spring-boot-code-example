package io.ngocnhan_tran1996.code.example.rabbitmq.rpc.testcase;

import io.ngocnhan_tran1996.code.example.testcase.TestArguments;

class RPCListenerTestArguments {

  static TestArguments testRPCArguments = TestArguments
      .params("Hello from RabbitMQ!", "Hello from RabbitMQ!_RPC")
      .nextParams(null, "NPE was raised", NullPointerException.class)
      .nextParams("", "_RPC");

}