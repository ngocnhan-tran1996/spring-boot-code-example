package io.ngocnhan_tran1996.code.example.aspect.testcase;

import io.ngocnhan_tran1996.code.example.testcase.TestArguments;

class AspectTestArguments {

  static TestArguments testAspectArguments = TestArguments
      .params(new RuntimeException("Test"), RuntimeException.class)
      .nextParams(new NullPointerException(), NullPointerException.class)
      .nextParams(new MyException(), MyException.class);

  static class MyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }
}