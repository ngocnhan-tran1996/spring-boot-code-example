package io.ngocnhan_tran1996.code.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Strings {

    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final String COMMA = ",";
    public static final String NULL_VALUE = "null";

    private static final ObjectMapper MAPPER = JsonMapper.builder()
        .findAndAddModules()
        .build();

    public static boolean isBlank(final String input) {

        return input == null
            || input.isBlank();
    }

    public static String join(final Object... strings) {

        if (strings == null) {

            return null;
        }

        var text = new StringBuilder();
        Stream.of(strings)
            .forEach(text::append);

        return text.toString();
    }

    public static String toSafeString(final Object object) {

        if (object == null) {

            return NULL_VALUE;
        }

        if (object instanceof String text) {

            return text;
        }

        try {

            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {

            return NULL_VALUE;
        }
    }

    public static String getIfNotBlank(final String... values) {

        return stream(values)
            .filter(Strings::isNotBlank)
            .findFirst()
            .orElse(null);
    }

    private static Stream<String> stream(final String... values) {

        return Optional.ofNullable(values)
            .map(Arrays::stream)
            .orElse(Stream.empty());
    }

    public static boolean isNotBlank(final String input) {

        return !isBlank(input);
    }

}