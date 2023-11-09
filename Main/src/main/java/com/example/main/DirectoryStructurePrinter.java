package com.example.main;

import java.io.File;

public class DirectoryStructurePrinter {

    public static void main(String[] args) {
        String rootPath = "/Users/lvmeijuan/gloryfares/provider-aggregator-parent";  // 替换为你的根目录路径
        File root = new File(rootPath);

        if (root.exists() && root.isDirectory()) {
            printDirectory(root, "");
        } else {
            System.out.println(rootPath + " 不是一个目录或不存在。");
        }
    }

    private static void printDirectory(File dir, String indent) {
        if (isSingleSubdirectory(dir)) {
            // 递归地合并单一目录
            File subDir = dir.listFiles(File::isDirectory)[0];
            printDirectory(subDir, indent + dir.getName() + "/");
        } else {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    // 忽略指定的文件和目录
                    if ((file.isDirectory() && shouldIgnoreDirectory(file)) || 
                        (file.isFile() && shouldIgnoreFile(file))) {
                        continue;
                    }
                    System.out.println(indent + (file.isDirectory() ? dir.getName() + "/" : "") + file.getName());
                    if (file.isDirectory()) {
                        printDirectory(file, indent + "    ");
                    }
                }
            }
        }
    }

    private static boolean isSingleSubdirectory(File dir) {
        File[] files = dir.listFiles();
        return files != null && files.length == 1 && files[0].isDirectory();
    }

    // ... shouldIgnoreFile 和 shouldIgnoreDirectory 方法 ...
    private static boolean shouldIgnoreFile(File file) {
        String[] ignoredExtensions = new String[]{"txt", "md"};  // 在这里添加你想要忽略的文件扩展名

        for (String extension : ignoredExtensions) {
            if (file.getName().toLowerCase().endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }

    private static boolean shouldIgnoreDirectory(File dir) {
        String[] ignoredDirectories = new String[]{"ignoredFolder1", "ignoredFolder2"};  // 在这里添加你想要忽略的文件夹名称

        for (String ignoredDir : ignoredDirectories) {
            if (dir.getName().equalsIgnoreCase(ignoredDir)) {
                return true;
            }
        }
        return false;
    }

}
