package com.springboot.code.example.rabbitmq.listener.testcase;

import java.util.List;
import com.springboot.code.example.testcase.TestArguments;

class ListenerTestArguments {

  static List<TestArguments<String, String>> testListenerArguments = List.of(
      TestArguments.ofSame("Hello from RabbitMQ!"),
      TestArguments.of(null, "NPE was raised", NullPointerException.class),
      TestArguments.ofSame(""));

}
