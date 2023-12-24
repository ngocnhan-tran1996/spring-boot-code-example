package com.springboot.code.example.rabbitmq.rpc.testcase;

import com.springboot.code.example.testcase.TestArguments;

class RPCListenerTestArguments {

  static TestArguments testRPCArguments = TestArguments
      .params("Hello from RabbitMQ!", "Hello from RabbitMQ!_RPC")
      .nextParams(null, "NPE was raised", NullPointerException.class)
      .nextParams("", "_RPC");

}