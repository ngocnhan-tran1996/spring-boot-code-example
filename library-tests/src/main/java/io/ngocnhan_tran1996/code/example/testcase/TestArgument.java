package io.ngocnhan_tran1996.code.example.testcase;

record TestArgument<I, O>(I input, O output, Class<? extends Throwable> exceptionClass) {

    static <I, O> TestArgument<I, O> of(
        I input,
        O output,
        Class<? extends Throwable> exceptionClass) {

        return new TestArgument<>(input, output, exceptionClass);
    }

    static <I, O> TestArgument<I, O> of(I input, O output) {

        return of(input, output, null);
    }

    static <I> TestArgument<I, I> ofSame(I input) {

        return of(input, input);
    }

}