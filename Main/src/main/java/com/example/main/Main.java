package com.example.main;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lvmeijuan
 */
public class Main {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        Map<String, String> map = list.stream()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        System.out.println(map);
    }
}
