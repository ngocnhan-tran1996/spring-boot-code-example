package com.springboot.code.example.log4j2.rewrite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.IntStream;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.SimpleMessage;
import com.springboot.code.example.common.helper.Strings;

@Plugin(name = "MaskRewritePolicy", category = Core.CATEGORY_NAME, elementType = "rewritePolicy")
public final class MaskRewritePolicy extends AbstractMaskRewritePolicy {

  private static final Pattern FIELD_NAME_PATTERN = Pattern.compile("^(?:\\w+(?:,\\w+)?)*+$");
  private static final String FIELD_NAME_REGEX =
      "\\\\?\"?(?:%s)\\\\?\"? ?[:=] ?\\\\?\"(.*)\\\\?\"";

  private final List<Pattern> patterns = new ArrayList<>();

  public MaskRewritePolicy(List<Pattern> patterns) {

    this.patterns.addAll(patterns);
  }

  @Override
  public LogEvent rewrite(final LogEvent event) {

    String formattedMessage = event.getMessage()
        .getFormattedMessage();
    var messageBuilder = new StringBuilder(formattedMessage);
    this.mask(messageBuilder);

    var simpleMessage = new SimpleMessage(messageBuilder.toString());
    return new Log4jLogEvent.Builder(event)
        .setMessage(simpleMessage)
        .build();
  }

  @PluginFactory
  public static MaskRewritePolicy createMaskRewritePolicy(
      @PluginValue("keywords") final String keywords,
      @PluginValue("patterns") final String patterns) {

    if (Strings.isAllBlank(keywords, patterns)) {

      return null;
    }

    List<Pattern> regexPatterns = new ArrayList<>();
    Optional.ofNullable(keywords)
        .map(FIELD_NAME_PATTERN::matcher)
        .map(Matcher::matches)
        .filter(Boolean::booleanValue)
        .ifPresent(result -> {

          var keywordRegex = String.format(
              FIELD_NAME_REGEX,
              keywords.replace(Strings.COMMA, "|"));

          addRegexPattern(keywordRegex, regexPatterns);
        });

    Optional.ofNullable(patterns)
        .map(p -> p.split(Strings.COMMA))
        .map(Arrays::asList)
        .orElse(Collections.emptyList())
        .stream()
        .forEach(pattern -> addRegexPattern(pattern, regexPatterns));

    return regexPatterns.isEmpty()
        ? null
        : new MaskRewritePolicy(regexPatterns);
  }

  @Override
  protected void mask(StringBuilder stringBuilder) {

    this.patterns.forEach(pattern -> {

      var matcher = pattern.matcher(stringBuilder);

      while (matcher.find()) {

        IntStream.rangeClosed(1, matcher.groupCount())
            .filter(group -> matcher.group(group) != null)
            .forEach(group -> IntStream.range(matcher.start(group), matcher.end(group))
                .forEach(i -> stringBuilder.setCharAt(i, this.getMaskCharacter())));
      }
    });
  }

  @Override
  protected char getMaskCharacter() {

    return '*';
  }

  private static void addRegexPattern(String regex, List<Pattern> patterns) {

    try {

      patterns.add(Pattern.compile(regex));
    } catch (PatternSyntaxException e) {
      // do nothing
    }
  }

}