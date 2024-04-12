package com.github.houbb.minicat.support.attr;

import com.github.houbb.heaven.annotation.CommonEager;

import java.util.*;

/**
 * @since 0.7.0
 */
public class DefaultMiniCatAttrManager implements IMiniCatAttrManager {

    private final Map<String, Object> objectMap = new HashMap<>();

    @Override
    public Object getAttribute(String key) {
        return objectMap.get(key);
    }

    @Override
        public Enumeration<String> getAttributeNames() {
        return setToEnumeration(objectMap.keySet());
    }

    @Override
    public void setAttribute(String key, Object object) {
        objectMap.put(key, object);
    }

    @Override
    public Object removeAttribute(String key) {
        return objectMap.remove(key);
    }

    @CommonEager
    public static <T> Enumeration<T> setToEnumeration(Set<T> set) {
        final Iterator<T> iterator = set.iterator();
        return new Enumeration<T>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public T nextElement() {
                return iterator.next();
            }
        };
    }

}
