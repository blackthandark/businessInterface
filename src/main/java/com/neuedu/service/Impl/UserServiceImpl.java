package com.neuedu.service.Impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.exception.MyException;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.MD5Utils;
import com.neuedu.utils.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserInfoMapper userInfoMapper;
    @Override
    public ServerResponse login(String username, String password){
        //step1:参数非空校验
        if(username==null||username.equals("")){
            return ServerResponse.createServerResponseByFail(100,"用户名不能为空");
        }
        if(password==null||password.equals("")){
            return ServerResponse.createServerResponseByFail(100,"密码不能为空");
        }
        //step2:判断用户名是否存在
        int username_result=userInfoMapper.exsistUsername(username);
        if(username_result==0){
            return ServerResponse.createServerResponseByFail(101,"用户名不存在");
        }

        UserInfo userInfo_result=userInfoMapper.selectByUsernameAdPassword(username,password);
        if(userInfo_result==null){
            return ServerResponse.createServerResponseByFail(1,"密码错误");
        }
        userInfo_result.setPassword(null);
        return ServerResponse.createServerResponseBySuccess(userInfo_result);
    }

    @Override
    public ServerResponse register(UserInfo userInfo) {
        userInfo.setRole(1);
        //参数非空校验
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(100,"注册信息不能为空");
        }
        if(userInfo.getUsername()==null||"".equals(userInfo.getUsername())){
            return ServerResponse.createServerResponseByFail(100,"用户名不能为空");
        }
        if(userInfo.getPassword()==null||"".equals(userInfo.getPassword())){
            return ServerResponse.createServerResponseByFail(100,"密码不能为空");
        }
        if(userInfo.getEmail()==null||"".equals(userInfo.getEmail())){
            return ServerResponse.createServerResponseByFail(100,"邮箱不能为空");
        }

        int result=userInfoMapper.exsistUsername(userInfo.getUsername());
        if(result>0){
            return ServerResponse.createServerResponseByFail(1,"用户已存在");
        }
        int result_email=userInfoMapper.exsistEmail(userInfo.getEmail());
        if(result_email>0){
            return ServerResponse.createServerResponseByFail(2,"邮箱已注册");
        }
        String password=userInfo.getPassword();
        String md5password= MD5Utils.getMD5Code(password);
        userInfo.setPassword(md5password);
        int count=userInfoMapper.insert(userInfo);
        if(count>0){
            return ServerResponse.createServerResponseBySuccess("用户注册成功");
        }
        return ServerResponse.createServerResponseByFail(3,"用户注册失败");
    }

    @Override
    public ServerResponse checkValid(String str, String type) {
            if(str==null||"".equals(str)){
                return ServerResponse.createServerResponseByFail(3,"邮箱或用户名不能为空");
            }
            if(type==null||"".equals(type)){
                return ServerResponse.createServerResponseByFail(3,"校验参数类型不能为空");
            }
            if("username".equals(type)){
                int count=userInfoMapper.exsistUsername(str);
                if(count>0){
                    return ServerResponse.createServerResponseByFail(1,"用户名已存在");
                }
            }
            if("email".equals(type)){
                int count=userInfoMapper.exsistEmail(str);
                if(count>0){
                    return ServerResponse.createServerResponseByFail(2,"邮箱已注册");
                }
            }
            return ServerResponse.createServerResponseBySuccess("校验成功");

    }

    @Override
    public ServerResponse forget_get_question(String username) {
        //参数非空校验
        if(username==null||"".equals(username)){
            return ServerResponse.createServerResponseByFail(100,"用户名不能为空");
        }
        //校验username
        int result=userInfoMapper.exsistUsername(username);
        if(result==0){
            return ServerResponse.createServerResponseByFail(101,"用户名不存在，请重新输入");
        }
        //查找密保问题
        String question=userInfoMapper.selectQuestionByUsername(username);
        if(question==null||"".equals(question)){
            return ServerResponse.createServerResponseByFail(1,"该用户未设置密保问题");
        }
        return ServerResponse.createServerResponseBySuccess(null,question);
    }

    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {
        //参数校验
        if(username==null||"".equals(username)){
            return ServerResponse.createServerResponseByFail(100,"用户名不能为空");
        }
        if(question==null||"".equals(question)){
            return ServerResponse.createServerResponseByFail(100,"问题不能为空");
        }
        if(answer==null||"".equals(answer)){
            return ServerResponse.createServerResponseByFail(100,"答案不能为空");
        }
        //查询
        int result=userInfoMapper.selectByUsernameAdQuestionAndAnswer(username,question,answer);
        if(result==0){
            return ServerResponse.createServerResponseByFail(1,"问题答案错误");
        }
        //服务端生成token保存并将token返回给客户端
        String forgetToken= UUID.randomUUID().toString();
        TokenCache.set(username,forgetToken);
        return ServerResponse.createServerResponseBySuccess(forgetToken);
    }

    @Override
    public ServerResponse forget_reset_password(String username, String passwordNew, String forgetToken) {
        //参数校验
        if(username==null||"".equals(username)){
            return ServerResponse.createServerResponseByFail(100,"用户名不能为空");
        }
        if(passwordNew==null||"".equals(passwordNew)){
            return ServerResponse.createServerResponseByFail(100,"密码不能为空");
        }
        if(forgetToken==null||"".equals(forgetToken)){
            return ServerResponse.createServerResponseByFail(102,"token不能为空");
        }
        //token校验
        String token=TokenCache.get(username);
        if(token==null){
            return ServerResponse.createServerResponseByFail(103,"token已经失效");
        }
        if(!token.equals(forgetToken)){
            return ServerResponse.createServerResponseByFail(104,"非法的token");
        }
        int result=userInfoMapper.updateUserPasswordByUsername(username,MD5Utils.getMD5Code(passwordNew));
        //修改密码
        if(result>0){
            return ServerResponse.createServerResponseBySuccess("修改密码成功");
        }

        return ServerResponse.createServerResponseByFail(1,"修改密码操作失败");
    }

    @Override
    public ServerResponse reset_password(String username,String passwordOld, String passwordNew) {
        //参数非空校验
        if(passwordOld==null||"".equals(passwordOld)){
            return ServerResponse.createServerResponseByFail(100,"旧密码不能为空");
        }
        if(passwordNew==null||"".equals(passwordNew)){
            return ServerResponse.createServerResponseByFail(100,"新密码不能为空");
        }
        //根据username和passwordOld查询
        UserInfo userInfo=userInfoMapper.selectByUsernameAdPassword(username,MD5Utils.getMD5Code(passwordOld));
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(1,"旧密码输入错误");
        }
        //修改密码
        userInfo.setPassword(MD5Utils.getMD5Code(passwordNew));
        int result=userInfoMapper.updateUserBySelectActive(userInfo);
        if(result>0){
            return ServerResponse.createServerResponseBySuccess("修改密码成功");
        }
        return ServerResponse.createServerResponseByFail(1,"密码修改失败");
    }

    @Override
    public ServerResponse update_information(UserInfo user) {
        //参数非空校验
        if(user==null){
            return ServerResponse.createServerResponseByFail(100,"参数不能为空");
        }
        int result=userInfoMapper.exsistEmail(user.getEmail());
        if(result>0){
            return ServerResponse.createServerResponseByFail(2,"邮箱已注册");
        }
        //更新用户信息
        int result1=userInfoMapper.updateUserBySelectActive(user);
        if(result1>0){
            return ServerResponse.createServerResponseBySuccess("更新个人信息成功");
        }
        return ServerResponse.createServerResponseByFail(1,"更新个人信息失败");
    }

    @Override
    public UserInfo findUserInfoByUserId(Integer userId) {
        return userInfoMapper.selectByPrimaryKey(userId);
    }

}
