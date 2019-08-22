package com.neuedu.interceptors;

import com.alibaba.druid.support.json.JSONUtils;
import com.neuedu.common.ServerResponse;
import com.neuedu.consts.Const;
import com.neuedu.pojo.UserInfo;
import com.neuedu.utils.JsonUtils;
import com.qiniu.util.Json;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


/*
* 拦截前台需要登录的请求
* */
@Component
public class PortalAuthorityInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("===prehandle===");

        HttpSession session=request.getSession();
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);
        if(userInfo==null){
            response.reset();
            try {
                response.setHeader("Content-Type","text/html;charset=UTF-8");
                PrintWriter printWriter=response.getWriter();
                ServerResponse serverResponse=ServerResponse.createServerResponseByFail(1,"用户未登录，请登录");
                String json= JsonUtils.obj2String(serverResponse);
                printWriter.write(json);
                printWriter.flush();
                printWriter.close();

            }catch (IOException e){

            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        System.out.println("==posthandle====");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("===aftercompletion===");
    }
}
