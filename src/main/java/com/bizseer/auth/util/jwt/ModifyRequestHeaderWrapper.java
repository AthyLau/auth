package com.bizseer.auth.util.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * Function:
 *
 * @author liubing
 * Date: 2019/9/6 3:49 PM
 * @since JDK 1.8
 */
public class ModifyRequestHeaderWrapper extends HttpServletRequestWrapper {
    private Map<String, String> customHeaders;

    public ModifyRequestHeaderWrapper(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<String, String>(){{
            put("Content-Type", "application/json");
            put("Accept", "application/json");
            put("charset", "utf-8");
            put("Accept-Charset", "utf-8");
            put("Content-Language", "zh-CN");
        }};
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    public String getHeader(String name) {
        String value = this.customHeaders.get(name);
        if (value != null) {
            return value;
        }
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<>(customHeaders.keySet());
        Enumeration<String> enumeration = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            set.add(name);
        }
        return Collections.enumeration(set);
    }
}
