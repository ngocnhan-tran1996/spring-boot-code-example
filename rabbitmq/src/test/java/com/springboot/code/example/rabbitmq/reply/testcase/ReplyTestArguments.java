package com.springboot.code.example.rabbitmq.reply.testcase;

import com.springboot.code.example.testcase.TestArguments;

class ReplyTestArguments {

  static TestArguments testMessagePropsArguments = TestArguments
      .params("Hello from RabbitMQ!", "Hello from RabbitMQ!_Reply")
      .nextParams("", "_Reply");

}