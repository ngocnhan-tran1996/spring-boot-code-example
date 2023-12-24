package com.springboot.code.example.rabbitmq.listener.testcase;

import com.springboot.code.example.testcase.TestArguments;

class ListenerTestArguments {

  static TestArguments testReceiveArguments = TestArguments
      .params("Hello from RabbitMQ!")
      .nextParams(null, "NPE was raised", NullPointerException.class)
      .nextParams("");

  static TestArguments testRPCArguments = TestArguments
      .params("Hello from RabbitMQ!", "Hello from RabbitMQ!_RPC")
      .nextParams(null, "NPE was raised", NullPointerException.class)
      .nextParams("", "_RPC");

}