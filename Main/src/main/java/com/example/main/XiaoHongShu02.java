package com.example.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class XiaoHongShu02 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(reader.readLine()); // 读取一行输入

        while (T-- > 0) {
            String s = reader.readLine();
            System.out.println(canFormPalindrome(s) ? "YES" : "NO");
        }
        reader.close();
    }

    private static boolean canFormPalindrome(String s) {
        int[] charCounts = new int[256];
        for (char c : s.toCharArray()) {
            charCounts[c]++;
        }
        int oddCount = 0; // Count of characters with odd occurrences
        for (int count : charCounts) {
            if (count % 2 != 0) {
                oddCount++;
            }
        }

        return oddCount <= 1;
    }

}
