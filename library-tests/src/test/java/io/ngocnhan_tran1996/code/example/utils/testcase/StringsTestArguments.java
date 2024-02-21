package io.ngocnhan_tran1996.code.example.utils.testcase;

import io.ngocnhan_tran1996.code.example.testcase.TestArguments;
import java.math.BigDecimal;

class StringsTestArguments {

    static TestArguments testIsBlankArguments = TestArguments
        .params(null, true)
        .nextParams("", true)
        .nextParams(" ", true)
        .nextParams("null", false)
        .nextParams("not blank", false);

    static TestArguments testJoinArguments = TestArguments
        .params(null, null)
        .nextParams(new String[]{"Hello", "World"}, "HelloWorld")
        .nextParams(new String[]{null, null}, "nullnull")
        .nextParams(new String[]{"Hello", ",", " ", "World", "!"}, "Hello, World!")
        .nextParams(new Object[]{"Hello", 1, BigDecimal.ONE, 1.5}, "Hello111.5");

    static TestArguments testToSafeStringArguments = TestArguments
        .params(null, "null")
        .nextParams("HelloWorld")
        .nextParams(1, "1")
        .nextParams(new StringsTestArguments(), "null");

    static TestArguments testGetIfNotBlankArguments = TestArguments
        .params((String[]) null, null)
        .nextParams((String[]) null, null)
        .nextParams(new String[0], null)
        .nextParams(new String[]{null, "", " "}, null)
        .nextParams(new String[]{null, null, " "}, null)
        .nextParams(new String[]{null, "zz"}, "zz")
        .nextParams(new String[]{"abc"}, "abc")
        .nextParams(new String[]{null, "xyz"}, "xyz")
        .nextParams(new String[]{null, "xyz", "abc"}, "xyz");
}