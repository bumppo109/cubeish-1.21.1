package com.bumppo109.cubeish.util.registry;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Helpers {

    private Helpers() {}

    public static <E extends Enum<E>, V> Map<E, V> mapOf(
            Class<E> enumClass,
            Function<E, V> valueMapper
    ) {
        return mapOf(enumClass, e -> true, valueMapper);
    }

    public static <E extends Enum<E>, V> Map<E, V> mapOf(
            Class<E> enumClass,
            Predicate<? super E> keyPredicate,
            Function<E, V> valueMapper
    ) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(keyPredicate)
                .collect(Collectors.toMap(
                        Function.identity(),
                        valueMapper,
                        (a, b) -> {
                            throw new AssertionError("Duplicate enum key: " + a);
                        },
                        () -> new EnumMap<>(enumClass)
                ));
    }
}