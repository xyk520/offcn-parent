package com.offcn.user.service.impl;

import com.offcn.user.excpetion.UserException;
import com.offcn.user.mapper.TMemberAddressMapper;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.pojo.TMemberAddressExample;
import com.offcn.user.pojo.TMemberExample;
import com.offcn.user.service.UserService;
import enums.UserExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private TMemberMapper memberMapper;

    @Autowired(required = false)
    private TMemberAddressMapper memberAddressMapper;


    @Override
    public TMember login(String loginacct, String password) {
        //根据登录名获取用户
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(loginacct);
        List<TMember> tMembers = memberMapper.selectByExample(example);
        //password是明文密码，要和数据库中加密之后的进行匹配
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (tMembers !=null && tMembers.size() == 1){
            TMember tMember = tMembers.get(0);
            boolean matches = encoder.matches(password, tMember.getUserpswd());
            return matches ? tMember : null;
        }

        return null;
    }

    @Override
    public void registerUser(TMember member) {

       // 1. 验证手机号是否在数据表中存在
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(member.getLoginacct());
        List<TMember> tMembers = memberMapper.selectByExample(example);
        if(tMembers.size() > 0){
            // 当前用户已经存在
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }
        // 2 手机没有被注册
        // 2.1 密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        member.setUserpswd(encoder.encode(member.getUserpswd()));
        member.setAuthstatus("0"); // 未实名认证
        member.setAccttype("0");  // 个人用户
        member.setUsertype("0"); // 用户类型
        memberMapper.insert(member);


    }

    @Override
    public TMember findTMemberById(Integer id) {
        return memberMapper.selectByPrimaryKey(id);
    }


    @Override
    public List<TMemberAddress> findAddressByMemberId(Integer memberId) {
        TMemberAddressExample example = new TMemberAddressExample();
        TMemberAddressExample.Criteria criteria = example.createCriteria();
        criteria.andMemberidEqualTo(memberId);
        return memberAddressMapper.selectByExample(example);
    }
}
