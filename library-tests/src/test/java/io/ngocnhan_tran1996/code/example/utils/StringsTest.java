package io.ngocnhan_tran1996.code.example.utils;

import static org.assertj.core.api.Assertions.assertThat;

import io.ngocnhan_tran1996.code.example.testcase.TestCase;

class StringsTest {

    @TestCase
    void testIsBlank(String input, boolean expectedOutput) {

        assertThat(Strings.isBlank(input)).isEqualTo(expectedOutput);
    }

    @TestCase
    void testJoin(Object[] input, String expectedOutput) {

        assertThat(Strings.join(input)).isEqualTo(expectedOutput);
    }

    @TestCase
    void testToSafeString(Object input, String expectedOutput) {

        assertThat(Strings.toSafeString(input)).isEqualTo(expectedOutput);
    }

    @TestCase
    void testGetIfNotBlank(String[] input, String expectedOutput) {

        assertThat(Strings.getIfNotBlank(input)).isEqualTo(expectedOutput);
    }

    @TestCase("io.ngocnhan_tran1996.code.example.utils.testcase.StringsTestArguments#testIsBlankArguments")
    void testIsNotBlank_ByNotOutputTestIsBlankArguments(String input, boolean expectedOutput) {

        assertThat(Strings.isNotBlank(input)).isNotEqualTo(expectedOutput);
    }

}