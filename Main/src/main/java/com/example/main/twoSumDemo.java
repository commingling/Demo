package com.example.main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class twoSumDemo {

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> hashtable = new HashMap<Integer, Integer>();
        // 1. 遍历数组
        for (int i = 0; i < nums.length; i++) {
            // 2. 判断target - nums[i]是否在哈希表中
            if (hashtable.containsKey(target - nums[i])) {
                // 3. 如果在哈希表中，返回target - nums[i]的下标和当前下标
                return new int[] { hashtable.get(target - nums[i]), i };
            }
            // 4. 如果不在哈希表中，将当前元素的值和下标存入哈希表中
            hashtable.put(nums[i], i);
        }
        return new int[] {};
    }

}
