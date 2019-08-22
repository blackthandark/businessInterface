package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.exception.MyException;
import com.neuedu.pojo.UserInfo;

import java.util.List;

public interface IUserService {
    public ServerResponse login(String username, String password) throws MyException;
    public ServerResponse register(UserInfo userInfo);
    public ServerResponse checkValid(String str,String type);
    public ServerResponse forget_get_question(String username);
    public ServerResponse forget_check_answer(String username,String question,String answer);
    public ServerResponse forget_reset_password(String username,String passwordNew,String forgetToken);
    public ServerResponse reset_password(String username,String passwordOld,String passwordNew);
    public ServerResponse update_information(UserInfo user);
    public UserInfo findUserInfoByUserId(Integer userId);
}
