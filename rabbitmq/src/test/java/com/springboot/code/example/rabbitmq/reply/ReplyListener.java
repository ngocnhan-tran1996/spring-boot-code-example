package com.springboot.code.example.rabbitmq.reply;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;

interface ReplyListener {

  static class Reply implements ReplyListener {

    @RabbitListener(queues = "#{queue.name}")
    String reply(String msg) {

      return msg + "_Reply";
    }

  }

  static class Send implements ReplyListener {

    @RabbitListener(queues = "#{queue.name}")
    @SendTo("#{replyQueue.name}")
    String sendTo(String msg) {

      return msg + "_Reply";
    }

  }

}
