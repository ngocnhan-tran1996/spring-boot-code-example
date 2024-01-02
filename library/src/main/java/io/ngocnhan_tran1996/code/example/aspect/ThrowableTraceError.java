package io.ngocnhan_tran1996.code.example.aspect;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.function.Function;
import io.ngocnhan_tran1996.code.example.trace.ThrowableTrace;
import lombok.Getter;

final class ThrowableTraceError extends AssertionError {

  private static final long serialVersionUID = 1L;

  @Getter
  private final Throwable throwable;
  private final String stackTrace;

  static Function<ThrowableTrace, ThrowableTraceError> get(Throwable throwable) {

    return throwableTrace -> new ThrowableTraceError(throwable, throwableTrace);
  }

  private ThrowableTraceError(Throwable throwable, ThrowableTrace throwableTrace) {

    this.throwable = throwable;
    this.stackTrace = throwableTrace.getTraceAsString(throwable);
    setStackTrace(new StackTraceElement[0]);
  }

  @Override
  public String getMessage() {
    return Optional.ofNullable(this.throwable)
        .map(Throwable::getMessage)
        .orElse(null);
  }

  @Override
  public void printStackTrace(PrintWriter out) {
    out.println(this);
  }

  @Override
  public void printStackTrace(PrintStream out) {
    out.println(this);
  }

  @Override
  public String toString() {
    return this.stackTrace;
  }

}