package io.ngocnhan_tran1996.code.example.rabbitmq.batch.testcase;

import java.util.ArrayList;
import java.util.stream.IntStream;
import io.ngocnhan_tran1996.code.example.testcase.TestArguments;

class BatchTestArguments {

  static TestArguments testBatchArguments;

  static {

    var evenMessage = new ArrayList<String>();
    var oddMessage = new ArrayList<String>();
    IntStream.range(1, 11)
        .forEach(i -> {

          String msg = new StringBuilder()
              .append("Even Msg ")
              .append(i)
              .toString();
          evenMessage.add(msg);
          oddMessage.add(msg);
        });

    oddMessage.add("Even Msg 11");

    testBatchArguments = TestArguments
        .params(evenMessage, 0)
        .nextParams(oddMessage, 1);
  }

}