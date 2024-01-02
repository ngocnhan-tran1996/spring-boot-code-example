package io.ngocnhan_tran1996.code.example.rabbitmq.rpc;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

class RPCListener {

  @RabbitListener(queues = "#{queue.name}")
  String rpc(String msg) {

    return msg + "_RPC";
  }

}
