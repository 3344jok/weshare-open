package com.zhang26.weshareserver.util;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Service
public class CacheKeyGenerator implements KeyGenerator {
    public Object generate(Object o, Method method, Object... objects) {
        return method.getDeclaringClass().getName() + "_"
                + method.getName() + "_"
                + StringUtils.arrayToDelimitedString(objects, "_");
    }
}
