package com.neuedu.controller;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import com.neuedu.common.ServerResponse;
import com.neuedu.consts.Const;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.MD5Utils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
@CrossOrigin
@RestController
@RequestMapping(value = "/user/")
public class UserController {
    @Autowired
    IUserService userService;
    /*
    * 登录
    * */
    @RequestMapping(value="login.do")
    public ServerResponse login(String username,
                                String password,
                                HttpSession session){
        String md5password= MD5Utils.getMD5Code(password);
        ServerResponse serverResponse=userService.login(username,md5password);
        if(serverResponse.isSuccess()){
            UserInfo userInfo=(UserInfo)serverResponse.getData();
            session.setAttribute(Const.CURRENT_USER,userInfo);
        }
        return serverResponse;
    }
    /*
    * 注册
    * */
    @RequestMapping(value="register.do")
    public ServerResponse register(UserInfo userInfo,
                                HttpSession session){
        ServerResponse serverResponse=userService.register(userInfo);
        return serverResponse;
    }

    /*
    * 检查用户名是否有效
    * */
    @RequestMapping("check_valid.do")
    public ServerResponse check_valid(String str,
                                String type){
        ServerResponse serverResponse=userService.checkValid(str,type);

        return serverResponse;
    }

    /*
    * 获取登录用户信息
    * */
    @RequestMapping("get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);
        UserInfo userInfo1=new UserInfo();
        BeanUtils.copyProperties(userInfo,userInfo1);
        userInfo1.setPassword(null);
        userInfo1.setQuestion(null);
        userInfo1.setAnswer(null);
        userInfo1.setRole(null);
        return ServerResponse.createServerResponseBySuccess(userInfo1);
    }

    /*
    * 根据用户名查询密保问题
    * */
    @RequestMapping("forget_get_question.do")
    public ServerResponse forget_get_question(String username){
        ServerResponse serverResponse=userService.forget_get_question(username);
        return serverResponse;
    }

    /*
    * 提交问题答案
    * */
    @RequestMapping("forget_check_answer.do")
    public ServerResponse forget_check_answer(String username,
                                              String question,
                                              String answer){
        ServerResponse serverResponse=userService.forget_check_answer(username,question,answer);
        return serverResponse;
    }

    /*
    * 忘记密码重设密码
    * */
    @RequestMapping("forget_reset_password.do")
    public ServerResponse forget_reset_password(String username,
                                              String passwordNew,
                                              String forgetToken){
        ServerResponse serverResponse=userService.forget_reset_password(username,passwordNew,forgetToken);
        return serverResponse;
    }

    /*
    *登录中重置密码
    * */
    @RequestMapping("reset_password.do")
    public ServerResponse reset_password(String passwordOld,
                                         String passwordNew,
                                         HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse=userService.reset_password(userInfo.getUsername(),passwordOld,passwordNew);
        if(serverResponse.isSuccess()){
            UserInfo userInfo1=userService.findUserInfoByUserId(userInfo.getId());
            session.setAttribute(Const.CURRENT_USER,userInfo1);
        }
        return serverResponse;
    }

    /*
    * 登录状态更新个人信息
    * */
    @RequestMapping("update_information.do")
    public ServerResponse update_information(UserInfo user,
                                              HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);
        user.setId(userInfo.getId());
        ServerResponse serverResponse=userService.update_information(user);
        if(serverResponse.isSuccess()){
            UserInfo userInfo1=userService.findUserInfoByUserId(userInfo.getId());
            session.setAttribute(Const.CURRENT_USER,userInfo1);
        }
        return serverResponse;
    }

    /*
     * 获取当前登录用户的详细信息
     * */
    @RequestMapping("get_inforamtion.do")
    public ServerResponse get_inforamtion(HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);
        /*if(userInfo==null){
            return ServerResponse.createServerResponseByFail(10,"用户未登录，无法获取当前用户信息");
        }*/
        UserInfo userInfo1=userInfo;
        userInfo1.setPassword(null);
        return ServerResponse.createServerResponseBySuccess(userInfo1);
    }
/*
* 登出
* */
    @RequestMapping("logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createServerResponseBySuccess("退出成功");
    }
}
