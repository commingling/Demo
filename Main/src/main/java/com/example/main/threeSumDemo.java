package com.example.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class threeSumDemo {

    public static void main(String[] args) {

    }

    // 这个三数之和的算法遵循以下步骤：
    //
    // 排序：首先对数组进行排序。这是为了后续能够使用双指针技术，并有效地跳过重复的元素。
    //
    // 遍历数组：遍历每个元素作为可能的三元组的第一个元素。如果当前元素与前一个元素相同，则跳过以避免重复的三元组。
    //
    // 双指针：对于每个元素，初始化两个指针，一个指向当前元素的下一个元素，另一个指向数组的最后一个元素。这两个指针代表可能的三元组的其他两个元素。
    //
    // 寻找和为0的三元组：如果三个元素的和为0，将它们作为一个结果添加到结果列表中。然后移动指针跳过所有重复的元素。
    //
    // 调整指针：如果三个元素的和小于0，将左边的指针向右移动一位。如果和大于0，将右边的指针向左移动一位。
    //
    // 重复步骤：重复以上步骤，直到所有可能的组合都被考虑过。
    //
    // 整个算法的核心是排序和双指针技术，它们共同帮助我们有效地找出所有不重复的和为0的三元组。
    //
    // Arrays 的 作用

    public static List<List<Integer>> threeSum(int[] nums) {

        // 对数组进行排序
        Arrays.sort(nums);

        // 创建一个存储结果的列表
        List<List<Integer>> result = new ArrayList<>();

        // 遍历数组
        for (int i = 0; i < nums.length - 2; i++) {
            // 如果当前元素和前一个元素相等，则跳过本次循环
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            // 定义左右指针
            int left = i + 1;
            int right = nums.length - 1;

            // 当左指针小于右指针时，循环查找
            while (left < right) {
                // 计算左右指针指向元素的和
                int sum = nums[left] + nums[right] + nums[i];
                // 如果和为0，则将左右指针指向的元素添加到结果列表中
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    // 如果左右指针指向的元素相等，则将左指针右移，将右指针左移
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    // 将左指针右移，将右指针左移
                    left++;
                    right--;
                    // 如果和大于0，则将右指针左移
                } else if (sum < 0) {
                    left++;
                    // 如果和小于0，则将左指针右移
                } else {
                    right--;
                }
            }

        }

        return result;
    }

}
