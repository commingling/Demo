package com.example.main;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TranslationUpdater {
    public static void main(String[] args) {
        // Excel文件路径和列索引
        String excelFilePath = "/Users/lvmeijuan/Desktop/多语言初稿.xlsx";
        int column_index_for_key = 3; // “key”在第三列
        int column_index_for_en = 4; // 英语
        int column_index_for_zh_CN = 6; // 简体中文
        int column_index_for_zh_TW = 7; // 繁体中文
        int column_index_for_es = 8; // 西班牙语
        int column_index_for_de = 9; // 德语
        int column_index_for_no = 10; // 挪威语
        int column_index_for_it = 11; // 意大利语
        int column_index_for_ko = 12; // 韩语
        int column_index_for_fr = 11; // 法语

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
        updateJavaScriptFile(zhCNTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/zh_CN.js");
        updateJavaScriptFile(zhTWTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/zh_TW.js");
        updateJavaScriptFile(esTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/es.js");
        updateJavaScriptFile(enTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/en.js");
        updateJavaScriptFile(deTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/de.js");
        updateJavaScriptFile(noTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/no.js");
        updateJavaScriptFile(itTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/it.js");
        updateJavaScriptFile(koTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/ko.js");
        updateJavaScriptFile(frTranslations, "/Users/lvmeijuan/IdeaProjects/Demo/fr.js");
    }

    private static Map<String, String> readTranslations(String excelFilePath, int keyColumn, int translationColumn) {
        // 读取Excel文件中的翻译，返回Map
        Map<String, String> translations = new HashMap<>();
        try (FileInputStream file = new FileInputStream(excelFilePath);
             Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean isFirstRow = true;
            for (Row row : sheet) {
                // 跳过标题行
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                Cell keyCell = row.getCell(keyColumn);
                Cell translationCell = row.getCell(translationColumn);

                // 检查单元格是否为空
                if (keyCell != null && translationCell != null &&
                        keyCell.getCellType() != CellType.BLANK && translationCell.getCellType() != CellType.BLANK) {
                    translations.put(keyCell.getStringCellValue(), translationCell.getStringCellValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return translations;
    }

    private static void updateJavaScriptFile(Map<String, String> translations, String jsFilePath) {
        // 将翻译写入JavaScript文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(jsFilePath))) {
            writer.write("export default {\n");
            for (Map.Entry<String, String> entry : translations.entrySet()) {
                writer.write("    " + entry.getKey() + ": \"" + entry.getValue() + "\",\n");
            }
            writer.write("};\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
