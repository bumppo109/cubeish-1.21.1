package com.bumppo109.cubeish.util.registry;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class RegistryHelpers {

    public static <E extends Enum<E>, V> Map<E, V> mapOf(
            Class<E> enumClass,
            Predicate<E> keyPredicate,
            Function<E, V> valueMapper
    ) {
        EnumMap<E, V> map = new EnumMap<>(enumClass);

        for (E key : enumClass.getEnumConstants()) {
            if (keyPredicate.test(key)) {
                if (map.put(key, valueMapper.apply(key)) != null) {
                    throw new AssertionError("Duplicate enum key");
                }
            }
        }

        return map;
    }

    public static <K extends Enum<K>, V, R> Map<K, R> mapOf(
            Map<K, V> source,
            BiFunction<K, V, R> valueMapper
    ) {
        EnumMap<K, R> map =
                new EnumMap<>(source.keySet().iterator().next().getDeclaringClass());

        source.forEach((key, value) -> {
            if (map.put(key, valueMapper.apply(key, value)) != null) {
                throw new AssertionError("Duplicate enum key");
            }
        });

        return map;
    }
}
