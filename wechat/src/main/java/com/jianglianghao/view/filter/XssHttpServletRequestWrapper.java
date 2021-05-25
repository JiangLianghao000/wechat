package com.jianglianghao.view.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 拦截所有请求参数进行转义，防止xss攻击
 * @verdion
 * @date 2021/5/2316:07
 */

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {


    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) return null;

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }

        return encodedValues;
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) return null;

        return cleanXSS(value);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) return null;

        return cleanXSS(value);
    }

    //实现转义
    private String cleanXSS(String value) {
		StringBuilder buffer = new StringBuilder(value.length() + 16);
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
			 case '>':
				 buffer.append("&gt");// 转义大于号
				 break;
			 case '<':
				 buffer.append("&lt");// 转义小于号
				 break;
			 case '\'':
				 buffer.append("&#39");// 转义单引号
				 break;
			 case '\"':
				 buffer.append("&quot"); // 转义双引号
				 break;
			 case '&':
				 buffer.append("&amp");// 转义&
				 break;
			 default:
				 buffer.append(c);
				 break;
			 }
		}
		return buffer.toString();
    }
}
