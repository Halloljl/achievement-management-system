package org.hallo.ams.settings.web.interceptor;

import org.hallo.ams.commons.Constants.Contants;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器，对登录进行处理验证
 * 简单的验证可以使用Filter
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //登录验证
        //登录成功(session 中有 user) -》放行
        if (request.getSession().getAttribute(Contants.SESSION_USER)!=null){
            return true;
        }
        //登录失败-》跳转登录
        response.sendRedirect(request.getContextPath()+"/");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
