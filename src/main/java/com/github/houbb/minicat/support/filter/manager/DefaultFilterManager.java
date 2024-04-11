package com.github.houbb.minicat.support.filter.manager;

import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.support.servlet.manager.DefaultServletManager;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * filter 管理
 *
 * @since 0.6.0
 */
public class DefaultFilterManager implements IFilterManager {

    private static final Log logger = LogFactory.getLog(DefaultServletManager.class);

    protected final Map<String, Filter> filterMap = new HashMap<>();

    protected String baseDirStr;

    protected void doInit(String baseDirStr) {
        this.baseDirStr = baseDirStr;
    }

    @Override
    public void init(String baseDir) {
        if(StringUtil.isEmpty(baseDir)) {
            throw new MiniCatException("baseDir is empty!");
        }

        doInit(baseDir);
    }

    @Override
    public void register(String url, Filter filter) {
        logger.info("[MiniCat] register Filter, url={}, Filter={}", url, filter.getClass().getName());

        filterMap.put(url, filter);
    }

    @Override
    public Filter getFilter(String url) {
        return filterMap.get(url);
    }

    @Override
    public List<Filter> getMatchFilters(String url) {
        List<Filter> resultList = new ArrayList<>();

        for(Map.Entry<String, Filter> entry : filterMap.entrySet()) {
            String urlPattern = entry.getKey();
            if(url.matches(urlPattern)) {
                resultList.add(entry.getValue());
            }
        }

        return resultList;
    }


}
