package io.ngocnhan_tran1996.code.example.rabbitmq.json.testcase;

import java.util.List;
import io.ngocnhan_tran1996.code.example.testcase.TestArguments;
import io.ngocnhan_tran1996.code.example.rabbitmq.json.Chairman;
import io.ngocnhan_tran1996.code.example.rabbitmq.json.Employee;

class JsonListenerTestArguments {

  static TestArguments testListenArguments;
  static TestArguments testListenBatchArguments;
  static TestArguments testReceiveArguments;

  static {

    var anonymous = new Employee();
    anonymous.setFirstName("Anonymous");
    anonymous.setLastName("Hacker");

    var emp = new Employee();
    emp.setFirstName("Nhan");
    emp.setLastName("Ngoc");
    testListenArguments = TestArguments
        .params(anonymous, 0)
        .nextParams(emp, 0);

    testListenBatchArguments = TestArguments
        .params(List.of(anonymous, emp), 0);

    var chairman = new Chairman();
    chairman.setFullName("Tran Ngoc Nhan");
    chairman.setAge(18);
    testReceiveArguments = TestArguments
        .params(chairman, 0);
  }
}