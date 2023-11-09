package com.example.main;

import java.util.Scanner;

public class rotateTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

    }

    public void rotate(int[] nums, int k) {
        int n = nums.length;
        int[] newArr = new int[n];
        for (int i = 0; i < n; i++) {
            newArr[(i + k) % n] = nums[i];
        }
        System.arraycopy(newArr, 0, nums, 0, n);
    }

    public void rotate2(int[] nums, int k) {
        int n = nums.length; // 数组的长度
        k = k % n; // k取模n，避免不必要的旋转
        int count = gcd(k, n); // 计算k和n的最大公约数
        for (int start = 0; start < count; ++start) {
            int current = start; // 当前元素的下标
            int prev = nums[start]; // 当前元素的前一个值
            do {
                int next = (current + k) % n; // 下一个元素的下标
                int temp = nums[next]; // 下一个元素的值
                nums[next] = prev; // 将下一个元素置为当前元素的前一个值
                prev = temp; // 更新当前元素的值
                current = next; // 更新当前元素的下标
            } while (start != current); // 旋转操作循环执行
        }
    }

    public int gcd(int x, int y) { // 计算最大公约数
        return y > 0 ? gcd(y, x % y) // 递归计算最大公约数
        : x;
    }


    public void rotate3(int[] nums, int k) {
        int n = nums.length;
        k %= n;
        // 先反转整个数组
        reverse(nums, 0, n - 1);
        // 再反转前 k 个元素
        reverse(nums, 0, k - 1);
        // 最后反转后 n-k 个元素
        reverse(nums, k, n - 1);
    }

    // 反转数组元素
    public void reverse(int[] nums, int start, int end) {
        while (start < end) {
            // 交换数组元素
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            // 更新索引值，继续交换下一对数组元素
            start++;
            end--;
        }
    }
}
