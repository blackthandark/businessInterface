package com.neuedu.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyExceptionHandlerResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object handler, Exception e) {
        if(e instanceof MyException){
        MyException myException=(MyException)e;
        System.out.println(myException.getMessage());
        String error=myException.getDirector();
        String msg=myException.getMessage();
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("common/error");
        modelAndView.addObject("url",error);
        modelAndView.addObject("msg",msg);
        return modelAndView;
        }
        return null;
    }
}
