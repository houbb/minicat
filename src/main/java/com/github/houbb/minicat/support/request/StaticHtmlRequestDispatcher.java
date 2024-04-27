package com.github.houbb.minicat.support.request;

import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.bs.MiniCatBootstrap;
import com.github.houbb.minicat.dto.IMiniCatRequest;
import com.github.houbb.minicat.dto.IMiniCatResponse;
import com.github.houbb.minicat.support.context.MiniCatContextConfig;
import com.github.houbb.minicat.util.InnerHttpUtil;
import com.github.houbb.minicat.util.InnerResourceUtil;

/**
 * 静态页面
 * @author 老马啸西风
 */
public class StaticHtmlRequestDispatcher implements IRequestDispatcher {

    private static final Log logger = LogFactory.getLog(StaticHtmlRequestDispatcher.class);

    public void dispatch(final IMiniCatRequest request,
                         final IMiniCatResponse response,
                         final MiniCatContextConfig config) {

        String absolutePath = InnerResourceUtil.buildFullPath(config.getBaseDir(), request.getUrl());
        String content = FileUtil.getFileContent(absolutePath);
        logger.info("[MiniCat] static html path: {}, content={}", absolutePath, content);
        String html = InnerHttpUtil.http200Resp(content);
        response.write(html);
    }

}
