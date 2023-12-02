package com.springboot.code.example.rabbitmq.confirms.testcase;

import java.util.List;
import java.util.UUID;
import org.springframework.amqp.rabbit.connection.CorrelationData.Confirm;
import com.springboot.code.example.testcase.TestArguments;
import com.springboot.code.example.utils.Strings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ConfirmsReturnsTestArguments {

  static List<TestArguments<ConfirmsReturnsArgumentsInput, ConfirmsReturnsArgumentsOutput>> testConfirmsReturnsArguments;

  static {

    var id = UUID.randomUUID().toString();
    var argsInput = new ConfirmsReturnsArgumentsInput(
        Strings.EMPTY,
        Strings.EMPTY,
        "default exchange - a existent queue");
    var noRouteArgsInput = new ConfirmsReturnsArgumentsInput(
        Strings.EMPTY,
        "NON_EXISTENT_QUEUE",
        "default exchange - a non-existent queue");
    var nackArgsInput = new ConfirmsReturnsArgumentsInput(
        id,
        Strings.EMPTY,
        "non-existent exchange - expect nack");

    var ackConfirm = new Confirm(true, null);
    var reason =
        "channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange '%s' in vhost '/', class-id=60, method-id=40)"
            .formatted(id);
    var nackArgsOutput = new ConfirmsReturnsArgumentsOutput(0, new Confirm(false, reason));

    testConfirmsReturnsArguments = List.of(
        TestArguments.of(argsInput, new ConfirmsReturnsArgumentsOutput(1, ackConfirm)),
        TestArguments.of(noRouteArgsInput, new ConfirmsReturnsArgumentsOutput(-1, ackConfirm)),
        TestArguments.of(nackArgsInput, nackArgsOutput));
  }

  @Getter
  @RequiredArgsConstructor
  public static class ConfirmsReturnsArgumentsInput {

    private final String exchange;
    private final String queueName;
    private final String message;
  }

  @Getter
  @RequiredArgsConstructor
  public static class ConfirmsReturnsArgumentsOutput {

    private final Integer messageCount;
    private final Confirm confirm;
  }

}