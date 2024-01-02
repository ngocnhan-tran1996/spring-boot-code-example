package io.ngocnhan_tran1996.code.example.trace;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import io.ngocnhan_tran1996.code.example.utils.Strings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ThrowableTraceUtils {

  static final String JAVA_EXTENSION = Strings.join(Strings.DOT, "java");

  static Map<String, Set<Path>> getPathByNames() {

    try {

      var vistor = new SourceFileVisitor();
      Files.walkFileTree(Path.of(Strings.EMPTY), vistor);
      return vistor.pathsByName;
    } catch (IOException e) {

      throw new SourceNotFoundException("Path \"EMPTY\" was not found");
    }

  }

  static String getStackTrace(final Throwable throwable) {

    if (throwable == null) {

      return Strings.EMPTY;
    }

    final var stringWriter = new StringWriter();
    throwable.printStackTrace(new PrintWriter(stringWriter, true));
    return stringWriter.toString();
  }

}
