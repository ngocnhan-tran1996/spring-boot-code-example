package com.springboot.code.example.rabbitmq.json.testcase;

import java.util.List;
import com.springboot.code.example.rabbitmq.json.Chairman;
import com.springboot.code.example.rabbitmq.json.Employee;
import com.springboot.code.example.testcase.TestArguments;

class JsonListenerTestArguments {

  static List<TestArguments<Employee, Integer>> testListenArguments;
  static List<TestArguments<List<Employee>, Integer>> testListenBatchArguments;
  static List<TestArguments<Chairman, Integer>> testReceiveArguments;

  static {

    var anonymous = new Employee();
    anonymous.setFirstName("Anonymous");
    anonymous.setLastName("Hacker");

    var emp = new Employee();
    emp.setFirstName("Nhan");
    emp.setLastName("Ngoc");
    testListenArguments = List.of(
        TestArguments.of(anonymous, 0),
        TestArguments.of(emp, 0));

    testListenBatchArguments = List.of(
        TestArguments.of(List.of(anonymous, emp), 0));

    var chairman = new Chairman();
    chairman.setFullName("Tran Ngoc Nhan");
    chairman.setAge(18);
    testReceiveArguments = List.of(
        TestArguments.of(chairman, 0));
  }
}