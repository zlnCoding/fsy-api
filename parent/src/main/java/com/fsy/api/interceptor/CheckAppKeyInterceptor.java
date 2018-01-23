package com.fsy.api.interceptor;/**
 * Created by zln on 2017/12/3.
 */

import com.fsy.api.utils.Const;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述:
 * 安全校验拦截器
 *
 * @auth zln
 * @create 2017-12-03 10:29
 */
public class CheckAppKeyInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/html;charset=UTF-8");

        if (request.getParameter("timestamp") == null) {
            response.getWriter().print("{\"result\":\"0\",\"msg\":\"验证未通过!时间戳不对,或时间戳已过期\"}");
            return false;
        }
        Long timestamp = Long.valueOf(request.getParameter("timestamp"));
        //验证时间戳
        if (System.currentTimeMillis() - timestamp > 1000 * 60 * 30 || System.currentTimeMillis() - timestamp < 1000 * 60 * 30) {
            response.getWriter().print("{\"result\":\"0\",\"msg\":\"验证未通过!时间戳不对,或时间戳已过期\"}");
            return false;
        }
        if (Const.isCheck(request.getParameterMap())) {
            return super.preHandle(request, response, handler);
        } else {
            response.getWriter().print("{\"result\":\"0\",\"msg\":\"验证失败!\"}");

            return false;
        }
    }

}
