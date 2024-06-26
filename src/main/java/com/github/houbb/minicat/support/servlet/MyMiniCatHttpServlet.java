package com.github.houbb.minicat.support.servlet;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.dto.IMiniCatResponse;
import com.github.houbb.minicat.dto.MiniCatResponseBio;
import com.github.houbb.minicat.util.InnerHttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 仅用于测试
 *
 * @since 0.3.0
 * @author 老马啸西风
 */
public class MyMiniCatHttpServlet extends AbstractMiniCatHttpServlet {

    private static final Log logger = LogFactory.getLog(MyMiniCatHttpServlet.class);


    // 方法实现
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        logger.info("MyMiniCatServlet-get");
        // 模拟耗时
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String content = "MyMiniCatServlet-get";
        IMiniCatResponse miniCatResponse = (IMiniCatResponse) response;
        miniCatResponse.write(InnerHttpUtil.http200Resp(content));
        logger.info("MyMiniCatServlet-get-end");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String content = "MyMiniCatServlet-post";

        IMiniCatResponse miniCatResponse = (IMiniCatResponse) response;
        miniCatResponse.write(InnerHttpUtil.http200Resp(content));
    }

}
