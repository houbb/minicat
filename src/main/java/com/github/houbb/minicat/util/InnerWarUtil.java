package com.github.houbb.minicat.util;

import com.github.houbb.heaven.util.io.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @since 0.5.0
 */
public class InnerWarUtil {

    public static void main(String[] args) {
        String warFilePath = "C:\\Users\\Administrator\\Downloads\\simple-servlet-master\\simple-servlet-master\\target\\servlet.war"; // 要解压的 WAR 文件路径
        String destinationDirectory = "C:\\Users\\Administrator\\Downloads\\simple-servlet-master\\simple-servlet-master\\target\\servlet"; // 解压后的目标目录

        try {
            extractWar(warFilePath, destinationDirectory);
            System.out.println("WAR 文件解压成功！");
        } catch (IOException e) {
            System.err.println("解压 WAR 文件时出错：" + e.getMessage());
        }
    }

    public static void extractWar(String warFilePath, String destinationDirectory) throws IOException {
        File destinationDir = new File(destinationDirectory);
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Paths.get(warFilePath)))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                String filePath = destinationDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipInputStream, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipInputStream.closeEntry();
                entry = zipInputStream.getNextEntry();
            }
        }
    }

    private static void extractFile(ZipInputStream zipInputStream, String filePath) throws IOException {
        FileUtil.createFile(filePath);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = zipInputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }
}

