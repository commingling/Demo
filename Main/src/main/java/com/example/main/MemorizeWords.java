package com.example.main;

import java.util.HashSet;
import java.util.Scanner;

public class MemorizeWords {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // 单词数量
        scanner.nextLine(); // 读取换行符

        HashSet<String> rememberedWords = new HashSet<>();
        int rememberCount = 0;

        for (int i = 0; i < n; i++) {
            String word = scanner.nextLine();
            if (!rememberedWords.contains(word)) { // 如果单词没被记住
                rememberedWords.add(word); // 记住新单词
                rememberCount++; // 记住的单词数量加一
            }
        }

        System.out.println(rememberCount); // 输出记住的单词数量
        scanner.close();
    }
}
