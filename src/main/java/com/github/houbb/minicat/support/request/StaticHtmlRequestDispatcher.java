package com.github.houbb.minicat.support.request;

import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.bs.MiniCatBootstrap;
import com.github.houbb.minicat.dto.MiniCatRequest;
import com.github.houbb.minicat.dto.MiniCatResponse;
import com.github.houbb.minicat.util.InnerHttpUtil;
import com.github.houbb.minicat.util.ResourceUtil;

/**
 * 静态页面
 */
public class StaticHtmlRequestDispatcher implements IRequestDispatcher {

    private static final Log logger = LogFactory.getLog(MiniCatBootstrap.class);

    /**
     * 请求分发
     *
     * @param context 上下文
     */
    public void dispatch(RequestDispatcherContext context) {
        final MiniCatRequest request = context.getRequest();
        final MiniCatResponse response = context.getResponse();

        String absolutePath = ResourceUtil.buildFullPath(ResourceUtil.getClassRootResource(MiniCatBootstrap.class), request.getUrl());
        String content = FileUtil.getFileContent(absolutePath);
        logger.info("[MiniCat] static html path: {}, content={}", absolutePath, content);
        String html = InnerHttpUtil.http200Resp(content);
        response.write(html);
    }

}
