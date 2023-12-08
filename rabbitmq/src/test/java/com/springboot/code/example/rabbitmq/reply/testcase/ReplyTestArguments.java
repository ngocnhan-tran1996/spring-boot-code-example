package com.springboot.code.example.rabbitmq.reply.testcase;

import java.util.List;
import com.springboot.code.example.testcase.TestArguments;

class ReplyTestArguments {

  static List<TestArguments<String, String>> testMessagePropsArguments = List.of(
      TestArguments.of("Hello from RabbitMQ!", "Hello from RabbitMQ!_Reply"),
      TestArguments.of("", "_Reply"));

}