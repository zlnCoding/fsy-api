package fsy.api.interceptor;/**
 * Created by zln on 2017/12/3.
 */

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
        return super.preHandle(request, response, handler);
    }
}
