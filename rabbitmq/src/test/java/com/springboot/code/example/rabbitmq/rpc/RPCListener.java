package com.springboot.code.example.rabbitmq.rpc;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

class RPCListener {

  @RabbitListener(queues = "#{queue.name}")
  String rpc(String msg) {

    return msg + "_RPC";
  }

}
