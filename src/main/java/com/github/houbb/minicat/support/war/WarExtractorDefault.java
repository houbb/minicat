package com.github.houbb.minicat.support.war;

import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.util.ArrayUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.util.InnerResourceUtil;
import com.github.houbb.minicat.util.InnerWarUtil;

import java.io.File;
import java.io.IOException;

/**
 * 默认的 war 处理
 *
 * @since 0.5.0
 */
public class WarExtractorDefault implements IWarExtractor {

    private static final Log logger = LogFactory.getLog(WarExtractorDefault.class);

    @Override
    public void extract(String baseDirStr) {
        logger.info("[MiniCat] start extract baseDirStr={}", baseDirStr);

        //1. check
//        Path baseDir = Paths.get(baseDirStr);
        File baseDirFile = new File(baseDirStr);
        if (!baseDirFile.exists()) {
            logger.error("[MiniCat] base dir not found!");
            throw new MiniCatException("baseDir not found!");
        }

        //2. list war
        File[] files = baseDirFile.listFiles();
        if (ArrayUtil.isNotEmpty(files)) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.endsWith(".war")) {
                    logger.error("[MiniCat] start extract war={}", fileName);
                    handleWarFile(baseDirStr, file);
                }
            }
        }

        // handle war package
        logger.info("[MiniCat] end extract baseDir={}", baseDirStr);
    }

    /**
     * 处理 war 文件
     *
     * @param baseDirStr 基础
     * @param warFile    war 文件
     */
    private void handleWarFile(String baseDirStr,
                               File warFile) {
        //1. 删除历史的 war 解压包
        String warPackageName = FileUtil.getFileName(warFile.getName());
        String fullWarPackagePath = InnerResourceUtil.buildFullPath(baseDirStr, warPackageName);
        FileUtil.deleteFileRecursive(fullWarPackagePath);

        //2. 解压文件
        try {
            InnerWarUtil.extractWar(warFile.getAbsolutePath(), fullWarPackagePath);
        } catch (IOException e) {
            logger.error("[MiniCat] handleWarFile failed, warPackageName={}", warFile.getAbsoluteFile(), e);
            throw new MiniCatException(e);
        }
    }

}
