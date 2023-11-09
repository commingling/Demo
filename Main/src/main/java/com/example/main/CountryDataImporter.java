package com.example.main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @author Quinlan
 */
public class CountryDataImporter {
    public static void main(String[] args) {
        String url = "jdbc:mysql://bj-cdb-6nh5tedc.sql.tencentcdb.com:63640/trips?characterEncoding=utf-8"; // 修改为你的数据库 URL
        String user = "trips"; // 数据库用户名
        String password = "dvQsll3kePe7OqgSQ,V^"; // 数据库密码

//        String content = "/Users/lvmeijuan/Desktop/telephoneAreaCodes.js"; // JSON 文件路径

        try {
            String content = new String(Files.readAllBytes(Paths.get("/Users/lvmeijuan/IdeaProjects/Demo/telephoneAreaCodes.js")), StandardCharsets.UTF_8);

            JSONArray countries = new JSONArray(content);


            Connection conn = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO infra_country_lang (c_code, area_code, zh_cn, zh_tw, en, ko, es, fr, it, de, nb_no) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < countries.length(); i++) {
                JSONObject country = countries.getJSONObject(i);
                pstmt.setString(1, country.getString("code"));
                pstmt.setString(2, country.getString("areaCode"));
                pstmt.setString(3, country.optString("ZH_CN"));
                pstmt.setString(4, country.optString("ZH_TW"));
                pstmt.setString(5, country.optString("EN"));
                pstmt.setString(6, country.optString("KO"));
                pstmt.setString(7, country.optString("ES"));
                pstmt.setString(8, country.optString("FR"));
                pstmt.setString(9, country.optString("IT"));
                pstmt.setString(10, country.optString("DE"));
                pstmt.setString(11, country.optString("NB_NO"));

                pstmt.executeUpdate();
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Country {
        String c_code;
        String area_code;
        String zh_CN;
        String zh_TW;
        String en;
        String ko;
        String ar;
        String it;
        String es;
        String de;
        String nb_no;
        String fr;
        // 其他字段...
    }
}
