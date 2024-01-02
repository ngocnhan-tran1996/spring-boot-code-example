package io.ngocnhan_tran1996.code.example.trace;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import io.ngocnhan_tran1996.code.example.utils.CollectionUtils;
import io.ngocnhan_tran1996.code.example.utils.Strings;

public class ThrowableTrace implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final String TAB_INDENTATION = "\t";
  private static final String DOUBLE_SPACE = Strings.SPACE.repeat(2);
  private static final Map<String, Set<Path>> FILES = ThrowableTraceUtils.getPathByNames();

  public String getTraceAsString(Throwable e) {

    if (e.getStackTrace() == null) {

      return null;
    }

    var stackTraceBuilder = new StringBuilder(ThrowableTraceUtils.getStackTrace(e));

    Arrays.stream(e.getStackTrace())
        .filter(element -> Objects.nonNull(element.getFileName()))
        .filter(element -> element.getLineNumber() > 0)
        .forEach(element -> {

          var fileName = element.getFileName();
          var paths = FILES.get(fileName);
          if (paths == null) {

            return;
          }

          var line = element.toString();
          var lineNumber = element.getLineNumber();
          var className = element.getClassName();

          var i = className.lastIndexOf(Strings.DOT);
          var slashClassName = i < 0
              ? Strings.EMPTY
              : className.substring(0, i)
                  .replace(Strings.DOT, Strings.SLASH);
          var fullyFileNamePath = Path.of(slashClassName, fileName);

          var lines = FILES.get(element.getFileName())
              .stream()
              .filter(path -> path.endsWith(fullyFileNamePath))
              .map(path -> readLines(lineNumber, path))
              .filter(CollectionUtils::isNotEmpty)
              .flatMap(linesByPath -> linesByPath.entrySet().stream())
              .map(entry -> {

                var target = entry.getKey() == lineNumber
                    ? "->"
                    : DOUBLE_SPACE;

                return String.format(
                    "%s %s%s",
                    target,
                    entry.getKey(),
                    Strings.join(DOUBLE_SPACE, entry.getValue()));
              })
              .collect(Collectors.joining(
                  System.lineSeparator() + TAB_INDENTATION,
                  TAB_INDENTATION,
                  Strings.EMPTY));
          var replacement = String.format("%s%n%n%s%n%n", line, lines);
          stackTraceBuilder.replace(
              stackTraceBuilder.indexOf(line),
              stackTraceBuilder.indexOf(line) + line.length(),
              replacement);
        });

    return stackTraceBuilder.toString();
  }

  private static NavigableMap<Integer, String> readLines(int lineNumber, Path path) {

    var output = new TreeMap<Integer, String>();
    try (var stream = Files.lines(Objects.requireNonNull(path, "Path must not be null"))) {

      var lines = stream
          // more 2 lines after exception line
          .limit(lineNumber + 2l)
          // more 2 lines before exception line
          .skip(lineNumber - 3l)
          .collect(Collectors.toCollection(ArrayList::new));

      var codeByLine = IntStream.range(0, lines.size())
          .boxed()
          .reduce(
              new TreeMap<Integer, String>(),
              (accumulator, i) -> {
                // subtract 2 for getting exactly line number
                accumulator.put(i + lineNumber - 2, lines.get(i));
                return accumulator;
              },
              (existence, replacement) -> replacement);

      // remove blank line from start
      IntStream.rangeClosed(codeByLine.firstKey(), codeByLine.lastKey())
          .takeWhile(i -> Strings.isBlank(codeByLine.get(i)))
          .forEach(codeByLine::remove);

      // remove blank line from end
      IntStream.iterate(codeByLine.lastKey(), i -> i >= 0, i -> i - 1)
          .takeWhile(i -> Strings.isBlank(codeByLine.get(i)))
          .forEach(codeByLine::remove);

      return codeByLine;
    } catch (IOException e) {

      return output;
    }

  }

}