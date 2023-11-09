package com.example.main;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelToJsConverter {
    public static void main(String[] args) throws IOException {
        FileInputStream file = new FileInputStream("/Users/lvmeijuan/IdeaProjects/Demo/新系统文案翻译.xlsx");
        // 定义列索引
        int column_index_for_key = 2; // 假设“key”在第三列
        int column_index_for_simplified_chinese = 4; // 假设简体中文翻译在第五列

        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheetAt(1);

        Map<String, String> translations = new HashMap<>();
        for (Row row : sheet) {
            Cell keyCell = row.getCell(column_index_for_key);
            Cell translationCell = row.getCell(column_index_for_simplified_chinese);

            if (keyCell != null && translationCell != null) {
                translations.put(keyCell.getStringCellValue(), translationCell.getStringCellValue());
            }
        }

        // Output translations in desired format (e.g., JavaScript object format)
        for (Map.Entry<String, String> entry : translations.entrySet()) {
            System.out.println("    " + entry.getKey() + ": \"" + entry.getValue() + "\",");
        }

        workbook.close();
    }
}
