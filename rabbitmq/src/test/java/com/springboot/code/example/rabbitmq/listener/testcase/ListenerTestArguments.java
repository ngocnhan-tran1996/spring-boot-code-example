package com.springboot.code.example.rabbitmq.listener.testcase;

import java.util.List;
import com.springboot.code.example.testcase.TestArguments;

class ListenerTestArguments {

  static List<TestArguments<String, String>> testReceiveArguments = List.of(
      TestArguments.ofSame("Hello from RabbitMQ!"),
      TestArguments.of(null, "NPE was raised", NullPointerException.class),
      TestArguments.ofSame(""));

  static List<TestArguments<String, String>> testRPCArguments = List.of(
      TestArguments.of("Hello from RabbitMQ!", "Hello from RabbitMQ!_RPC"),
      TestArguments.of(null, "NPE was raised", NullPointerException.class),
      TestArguments.of("", "_RPC"));

}
