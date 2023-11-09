package com.example.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class WrittenTest {
    private static final Logger log = LoggerFactory.getLogger(WrittenTest.class);
    private final Map<String, Integer> countMap = new ConcurrentHashMap<>(); // 使用 ConcurrentHashMap 以支持高并发

    /**
     * @param request HttpServletRequest对象，用于获取请求参数
     * @param fcNum   规定值
     * @return 三种值从Map获取计数值后，加起来是否超过规定值。
     */
    @RequestMapping("/imp/test")
    public boolean imptest(HttpServletRequest request, @RequestParam int fcNum) {
        // 问题1：ns的值只会有如上两种情况，将queryStr中ns的值都赋值为固定IP(110.1.3.4)，并打印queryStr。
        String queryStr = request.getQueryString();
        if (queryStr != null) {
            queryStr = queryStr.replaceAll("ns=([^&]*)", "ns=110.1.3.4");
            System.out.println(queryStr); // 打印修改后的queryStr
        }

        // 问题2：定义变量m2Chn赋值为m2的值加上固定字符串'_chn'（考虑m2若是null，当为空字符串）；
        String m2 = request.getParameter("m2");
        String m2Chn = (m2 == null ? "" : m2) + "_chn";

        int totalCount = 0;
        boolean isOverCount = false;

        // 使用同步代码块来确保线程安全
        synchronized (countMap) {
            try {
                // 更新m2计数并判断是否超过fcNum
                totalCount += updateCounter("m2_" + m2, 1);
                isOverCount = totalCount >= fcNum;

                // 如果没有超过，更新m2Chn计数并判断
                if (!isOverCount) {
                    totalCount += updateCounter("m2Chn_" + m2Chn, 1);
                    isOverCount = totalCount >= fcNum;
                }

                // 如果没有超过，更新m5计数并判断
                if (!isOverCount) {
                    String m5 = request.getParameter("m5");
                    totalCount += updateCounter("m5_" + m5, 1);
                    isOverCount = totalCount >= fcNum;
                }

            } catch (NumberFormatException e) {
                log.error("Error parsing count values", e);
            }
        }

        return isOverCount;
    }

    /**
     * 更新计数器的值并返回新的计数值。
     *
     * @param key   计数器的键
     * @param delta 增加的值
     * @return 新的计数值
     */
    private int updateCounter(String key, int delta) {
        return countMap.merge(key, delta, Integer::sum);
    }
}
