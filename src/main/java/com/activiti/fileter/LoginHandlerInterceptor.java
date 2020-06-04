package com.activiti.fileter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        try {
            //统一拦截（查询当前session是否存在user）(这里user会在每次登陆成功后，写入session)
            Object user= httpServletRequest.getSession().getAttribute("user");
            if(user!=null){
                return true;
            }
            httpServletResponse.sendRedirect("/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;//如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        //如果设置为true时，请求将会继续执行后面的操作
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
