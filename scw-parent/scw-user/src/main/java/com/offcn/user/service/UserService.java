package com.offcn.user.service;

import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;

import java.util.List;

public interface UserService {

    //注册
    public void registerUser(TMember member);

    //登录
    public TMember login(String loginacct,String password);

    //获取用户的信息
    public TMember findTMemberById(Integer id);

//    获取收货地址
    public List<TMemberAddress> findAddressByMemberId(Integer memberId);

}
