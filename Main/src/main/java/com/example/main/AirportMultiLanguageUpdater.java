package com.example.main;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Quinlan
 */
public class AirportMultiLanguageUpdater {
    private static final Logger logger = LoggerFactory.getLogger(AirportMultiLanguageUpdater.class);

    private static String api = "https://api.xiaoxiangdaili.com/ip/get?appKey=1082955524351610880&appSecret=W46zoxQ6&cnt=1&wt=json";

    public static Proxy get() {

        Request request = new Request.Builder()
                .url(String.format(api))
                .build();
        try(Response response = new OkHttpClient().newBuilder().build()
                .newCall(request).execute()) {

            logger.info("xiaoxiangdaili api response: " + response);
            if (response.isSuccessful()) {
                String content = response.body().string();
                Proxy proxy = null;
                logger.info("xiaoxiangdaili ip proxy pool api response: " + content);
                JSONObject result = JSONObject.parseObject(content);
                if (result.getInteger("code") == 200) {
                    JSONArray array = result.getJSONArray("data");
                    if (!CollectionUtils.isEmpty(array)) {
                        JSONObject j = array.getJSONObject(0);
                        content = j.getString("ip") + ":" +j.getString("port");
                        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(j.getString("ip"), Integer.parseInt(j.getString("port"))));

                    }

                    return proxy;
                } else {
                    return null;
                }
            }
        } catch (IOException e) {
            logger.error("xiaoxiangdaili ip proxy pool api call fail , error is  " + e.getMessage(),e);
        }
        return null;
    }



    private static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://bj-cdb-6nh5tedc.sql.tencentcdb.com:63640/trips?characterEncoding=utf-8";
        String username = "trips";
        String password = "dvQsll3kePe7OqgSQ,V^";
        return DriverManager.getConnection(url, username, password);
    }

    private static Map<String, String> getLanguageToUrlMap() {
        Map<String, String> map = new HashMap<>();
        map.put("ko", "https://kr.trip.com/flights/graphql/poiSearch");
        map.put("zh_TW", "https://hk.trip.com/flights/graphql/poiSearch");
        map.put("fr", "https://fr.trip.com/flights/graphql/poiSearch");
        map.put("es", "https://es.trip.com/flights/graphql/poiSearch");
        map.put("it", "https://it.trip.com/flights/graphql/poiSearch");
        map.put("de", "https://de.trip.com/flights/graphql/poiSearch");
        map.put("en", "https://www.trip.com/flights/graphql/poiSearch");
        return map;
    }

    private static void fetchAndInsertAirportLanguageDetails(Connection conn, long infraCcapId, String airportCode, String language)  {
        Map<String, String> languageToUrlMap = getLanguageToUrlMap();

        String url = languageToUrlMap.get(language);

        // 该方法负责根据airportCode和language发起HTTP请求，并将结果插入到infra_ccap_sub表中
        System.out.println("Updating language details for airport: " + airportCode + ", Language: " + language + ", URL: " + url);
        System.out.println("Updating language details for airport: " + airportCode + ", Language: " + language);
        Proxy proxy =    get();
        // HTTP请求发送和结果处理逻辑应根据实际API文档实现
        String requestBody = constructRequestBody(airportCode);
        // 根据API要求构造请求体
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(60, TimeUnit.SECONDS) // 设置读取超时时间为60秒
                .proxy(proxy)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"operationName\": \"poiSearch\",\r\n    \"variables\": {\r\n        \"key\": \""+
                airportCode +"\",\r\n        \"mode\": \"0\",\r\n        \"tripType\": \"RT\"\r\n    },\r\n    \"extensions\": {\r\n        \"persistedQuery\": {\r\n            \"version\": 1,\r\n            \"sha256Hash\": \"a04f920da8c6c0545bab401b5c2f3488878430a73c4b23aed9fda2156c7044d8\"\r\n        }\r\n    }\r\n}");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "de.trip.com")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie", "_combined=transactionId%3D4094b7add8c7e38182247f2cbcd30d74; _abtest_userid=36f243bd-1fa5-4c4b-8129-9bebb3cf6b33")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            // 检查响应状态码
            if (response.code() != 200) {
                // 如果状态码为503，打印日志并返回或执行其他逻辑
                System.out.println("503 Service Unavailable received, skipping this request.");
                return; // 直接返回，不执行后续操作
            }

            // 暂停一段时间（例如，1秒）
            Thread.sleep(1000); // 注意：这会使当前线程暂停执行


            // 获取响应体的字符串
            String responseBody = response.body().string();



        JSONObject jsonObject = JSON.parseObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject poiSearch = data.getJSONObject("poiSearch");
        JSONArray results = poiSearch.getJSONArray("results");
        String cityName = "";
        String name = "";
        String countryName = "";
        // 遍历 results
        for (int i = 0; i < results.size(); i++) {
            JSONObject result = results.getJSONObject(i);
            JSONArray childResults = result.getJSONArray("childResults");
            if (childResults != null) {
                // 遍历 childResults
                for (int j = 0; j < childResults.size(); j++) {
                    JSONObject childResult = childResults.getJSONObject(j);
                     cityName = childResult.getString("cityName");
                     name = childResult.getString("name");
                     countryName = childResult.getString("countryName");
                     airportCode = childResult.getString("airportCode");

                    System.out.println("City Name: " + cityName);
                    System.out.println("Name: " + name);
                    System.out.println("Country Name: " + countryName);
                    System.out.println("Airport Code: " + airportCode);
                    // 根据实际需求处理获取到的数据
                }
            }
        }
            // 确保数据库连接是自动提交的
            conn.setAutoCommit(true);


        // 插入或更新数据库
        String insertQuery = "INSERT INTO infra_ccap_sub (infra_ccap_id, airport_name, country_name, city_name,lang) VALUES (?, ?, ?, ?, ?);";

        System.out.println("Executing query: " + insertQuery);
        System.out.println("With parameters: " + infraCcapId + ", " + name + ", " + countryName + ", " + cityName + ", " + language);

        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setLong(1, infraCcapId);
            insertStmt.setString(2, name);
            insertStmt.setString(3, countryName);
            insertStmt.setString(4, cityName);
            insertStmt.setString(5, language);
            // 设置其他字段...
            insertStmt.executeUpdate();
        }
        } catch (Exception e) {
            return;
        }

    }


    private static String constructRequestBody(String airportCode) {
        // 根据实际需要构造请求体
        return String.format("{\"operationName\":\"poiSearch\",\"variables\":{\"key\":\"%s\",\"mode\":\"0\",\"tripType\":\"RT\"},\"extensions\":{\"persistedQuery\":{\"version\":1,\"sha256Hash\":\"a04f920da8c6c0545bab401b5c2f3488878430a73c4b23aed9fda2156c7044d8\"}}}", airportCode);
    }


    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            String query = """
                    SELECT ic.id AS infra_ccap_id, ic.airport_code, l.lang_code
                    FROM infra_ccap ic
                    CROSS JOIN (
                        SELECT 'en' AS lang_code
                        UNION ALL SELECT 'fr'
                    ) l
                    LEFT JOIN infra_ccap_sub ics ON ic.id = ics.infra_ccap_id AND l.lang_code = ics.lang
                    WHERE ics.id IS NULL;
                    """;
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long infraCcapId = rs.getLong("infra_ccap_id");
                String airportCode = rs.getString("airport_code");
                String langCode = rs.getString("lang_code");
                fetchAndInsertAirportLanguageDetails(conn, infraCcapId, airportCode, langCode);
                // 暂停1秒（1000毫秒）再发送下一个请求
                Thread.sleep(10000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
