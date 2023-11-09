package com.example.main.Excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TranslationUpdater {
    public static void main(String[] args) {
        // Excel文件路径和列索引
        String excelFilePath = "/Users/lvmeijuan/Desktop/多语言初稿.xlsx";
        int column_index_for_key = 3; // “key”在第三列
        int column_index_for_en = 4; // 英语
        int column_index_for_zh_CN = 5; // 简体中文
        int column_index_for_zh_TW = 6; // 繁体中文
        int column_index_for_es = 7; // 西班牙语
        int column_index_for_de = 8; // 德语
        int column_index_for_no = 9; // 挪威语
        int column_index_for_it = 10; // 意大利语
        int column_index_for_ko = 11; // 韩语
        int column_index_for_fr = 12; // 法语

        // 读取翻译
        Map<String, String> zhCNTranslations = readTranslations(excelFilePath, column_index_for_key, column_index_for_zh_CN);
        Map<String, String> zhTWTranslations = readTranslations(excelFilePath, column_index_for_key, column_index_for_zh_TW);
        Map<String, String> esTranslations = readTranslations(excelFilePath, column_index_for_key, column_index_for_es);
        Map<String, String> enTranslations = readTranslations(excelFilePath, column_index_for_key, column_index_for_en);
        Map<String, String> deTranslations = readTranslations(excelFilePath, column_index_for_key, column_index_for_de);
        Map<String, String> noTranslations = readTranslations(excelFilePath, column_index_for_key, column_index_for_no);
        Map<String, String> itTranslations = readTranslations(excelFilePath, column_index_for_key, column_index_for_it);
        Map<String, String> koTranslations = readTranslations(excelFilePath, column_index_for_key, column_index_for_ko);
        Map<String, String> frTranslations = readTranslations(excelFilePath, column_index_for_key, column_index_for_fr);

        // 更新JavaScript文件
        updateJavaScriptFile(zhCNTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/Main/src/main/java/com/example/main/Excel/zh_CN.js");
        updateJavaScriptFile(zhTWTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/Main/src/main/java/com/example/main/Excel/zh_TW.js");
        updateJavaScriptFile(esTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/Main/src/main/java/com/example/main/Excel/es.js");
        updateJavaScriptFile(enTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/Main/src/main/java/com/example/main/Excel/en.js");
        updateJavaScriptFile(deTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/Main/src/main/java/com/example/main/Excel/de.js");
        updateJavaScriptFile(noTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/Main/src/main/java/com/example/main/Excel/no.js");
        updateJavaScriptFile(itTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/Main/src/main/java/com/example/main/Excel/it.js");
        updateJavaScriptFile(koTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/Main/src/main/java/com/example/main/Excel/ko.js");
        updateJavaScriptFile(frTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/Main/src/main/java/com/example/main/Excel/fr.js");
    }

    private static Map<String, String> readTranslations(String excelFilePath, int keyColumn, int translationColumn) {
        // 读取Excel文件中的翻译，返回Map
        Map<String, String> translations = new HashMap<>();
        try (FileInputStream file = new FileInputStream(excelFilePath);
             Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheetAt(2);
            boolean isFirstRow = true;
            for (Row row : sheet) {
                // 跳过标题行
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                Cell keyCell = row.getCell(keyColumn);
                Cell translationCell = row.getCell(translationColumn);

                // 检查单元格是否为空并获取单元格内容
                if (keyCell != null && translationCell != null &&
                    keyCell.getCellType() != CellType.BLANK && translationCell.getCellType() != CellType.BLANK) {
                
                    String key = getCellValueAsString(keyCell);
                    String translation = getCellValueAsString(translationCell);

                    // 检查键是否为null
                    if (key != null) {
                        // 替换键中的所有空白字符为下划线，并转义引号
                        key = key.replaceAll("\\s+", "").replace("\"", "\\\"");
                    } else {
                        // 处理key为null的情况，例如给一个默认值或跳过
                        key = "defaultKey"; // 或者选择跳过这个键值对
                    }

                    translations.put(key, translation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return translations;
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return Double.toString(cell.getNumericCellValue());
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    private static void updateJavaScriptFile(Map<String, String> translations, String jsFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jsFilePath), StandardCharsets.UTF_8))) {
            writer.write("export default {\n");
            for (Map.Entry<String, String> entry : translations.entrySet()) {
                String key = entry.getKey();
                // 检查键是否为null
                if (key != null) {
                    // 替换键中的所有空白字符为下划线，并转义引号
                    key = key.replaceAll("\\s+", "").replace("\"", "\\\"");
                } else {
                    // 处理key为null的情况，例如给一个默认值或跳过
                    key = "defaultKey"; // 或者选择跳过这个键值对
                }
                // 替换值中的非断行空格为普通空格，并转义引号和换行符
                String value = entry.getValue().replace("\u00A0", " ").replace("\"", "\\\"").replace("\n", "\\n");
                // 新增的替换逻辑
                value = value.replace("@", "{'@'}");

                // 检查value是否以"\n"开头，如果是就移除它
                if (value.startsWith("\n")) {
                    System.out.println(value+"1");
                    value = value.substring(1);
                    System.out.println(value+"2");
                }
                // 检查value是否以"\\n"开头，如果是就移除它
                if (value.startsWith("\\n")) {
                    System.out.println(value+"1");
                    value = value.substring(2);
                    System.out.println(value+"2");
                }

                // 写入处理后的键（没有双引号包围）和值
                writer.write("    " + key + ": \"" + value + "\",\n");
            }
            writer.write("};\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
