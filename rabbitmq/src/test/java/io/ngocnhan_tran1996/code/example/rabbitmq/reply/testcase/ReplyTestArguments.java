package io.ngocnhan_tran1996.code.example.rabbitmq.reply.testcase;

import io.ngocnhan_tran1996.code.example.testcase.TestArguments;

class ReplyTestArguments {

    static TestArguments testMessagePropsArguments = TestArguments
        .params("Hello from RabbitMQ!", "Hello from RabbitMQ!_Reply")
        .nextParams("", "_Reply");

}