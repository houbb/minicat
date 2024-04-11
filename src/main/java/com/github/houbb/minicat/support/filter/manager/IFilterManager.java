package com.github.houbb.minicat.support.filter.manager;

import javax.servlet.Filter;
import java.util.List;

/**
 * filter 管理
 *
 * @since 0.6.0
 */
public interface IFilterManager {

    /**
     * 初始化
     * @param baseDir 基础文件夹
     * @since 0.5.0
     */
    void init(String baseDir);

    /**
     * 注册 servlet
     *
     * @param url     url
     * @param filter servlet
     */
    void register(String url, Filter filter);

    /**
     * 获取 servlet
     *
     * @param url url
     * @return servlet
     */
    Filter getFilter(String url);

    /**
     * 获取匹配的
     * @param url 正则
     * @return 结果
     */
    List<Filter> getMatchFilters(String url);
}
