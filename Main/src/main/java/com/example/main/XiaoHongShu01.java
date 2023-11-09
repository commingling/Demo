package com.example.main;

import java.util.HashMap;
import java.util.Scanner;

public class XiaoHongShu01 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // 单词数量
        scanner.nextLine(); // 读取换行符

        HashMap<String, Integer> wordCount = new HashMap<>();
        int rememberedWords = 0;

        for (int i = 0; i < n; i++) {
            String word = scanner.nextLine();
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1); // 计数每个单词背诵的次数
            // 如果单词背诵次数等于当前记住的单词数加1，则记住这个单词
            if (wordCount.get(word) == rememberedWords + 1) {
                rememberedWords++;
            }
        }

        System.out.println(rememberedWords); // 输出记住的单词数量
        scanner.close();
    }
}
