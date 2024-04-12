package com.github.houbb.minicat.support.attr;

import java.util.Enumeration;

/**
 * 属性管理类
 *
 * @since 0.7.0
 */
public interface IMiniCatAttrManager {


    Object getAttribute(String key);

    /**
     * Returns an <code>Enumeration</code> containing the
     * names of the attributes available to this request.
     * This method returns an empty <code>Enumeration</code>
     * if the request has no attributes available to it.
     *
     * @return an <code>Enumeration</code> of strings containing the names
     * of the request's attributes
     */
    Enumeration<String> getAttributeNames();

    void setAttribute(String key, Object object);

    Object removeAttribute(String key);

}
