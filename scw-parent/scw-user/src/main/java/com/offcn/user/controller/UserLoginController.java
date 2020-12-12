package com.offcn.user.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.service.UserService;
import com.offcn.user.utils.SmsTemplate;
import com.offcn.user.vo.UserResistVo;
import com.offcn.user.vo.response.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "用户登录和注册模块")
public class UserLoginController {

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取验证码信息")
    @PostMapping("/sendSms")
    public AppResponse<Object> sendSms(String phoneNum){
        // 1.生成验证码
        String code = UUID.randomUUID().toString().substring(0, 4);
        System.out.println("当前验证码是:" + code);
        // 2. 将验证码存储在redis 中  5分钟有效
        redisTemplate.opsForValue().set(phoneNum,code,5, TimeUnit.MINUTES);
        // 3. 短信发送
        try {
            String okMsg = "ok";//smsTemplate.sendCode(phoneNum, code);
            return AppResponse.ok(okMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail("短信发送失败");
        }
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public AppResponse<UserRespVo> login(String loginacct,String password){
        //使用service完成登录
        TMember member = userService.login(loginacct, password);
        if (member == null){
            AppResponse<UserRespVo> fail = AppResponse.fail(null);
            fail.setMsg("用户名或者密码错误！！");
            return fail;
        }
        //登录成功
        //通过UUID为登录之后的用户创建令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        UserRespVo userRespVo = new UserRespVo();
        userRespVo.setAccessToken(token);
        //将service返回的tmember对象的属性值复制给当前的userrespvo
        BeanUtils.copyProperties(member, userRespVo);
        //将数据存储到Redis中
        redisTemplate.opsForValue().set(token, member.getId()+"",2, TimeUnit.HOURS);
        //返回结果
        return AppResponse.ok(userRespVo);

    }



    @ApiOperation(value = "用户注册")
    @PostMapping("/regist")
    public AppResponse<Object> register(UserResistVo userResistVo){
        // 1. 获取验证码
        // redis中存储的验证码
        String code = redisTemplate.opsForValue().get(userResistVo.getLoginacct());
        if(code != null && code.length() > 0){
            boolean flag = code.equalsIgnoreCase(userResistVo.getCode());//用户填入的验证码
            if(flag){
                // 完成注册
                TMember tMember = new TMember();
//                tMember.setLoginacct(userResistVo.getLoginacct());
//                tMember.setUserpswd(userResistVo.getUserpswd());
//                tMember.setEmail(userResistVo.getEmail());
                // 该方法要求属性名必须一致，否则不一样的属性赋值为空
                BeanUtils.copyProperties(userResistVo,tMember);
                userService.registerUser(tMember);
                // 删除验证码
                redisTemplate.delete(tMember.getLoginacct());
                return AppResponse.ok("注册成功");
            }else{
                // 用户输入的验证码和存储的验证码不一致
                return AppResponse.fail("验证码错误");
            }
        }else{
            return AppResponse.fail("当前验证码失效");
        }

    }

    @ApiOperation(value = "通过id获取用户信息")
    @GetMapping("/findUser/{id}")
    public AppResponse<UserRespVo> findUserById(@PathVariable("id")Integer id){
        TMember tMember = userService.findTMemberById(id);
        UserRespVo userRespVo = new UserRespVo();
        BeanUtils.copyProperties(tMember, userRespVo);
        return AppResponse.ok(userRespVo);
    }


    @ApiOperation(value = "获取当前用户登录的收货地址")
    @GetMapping("/findAddressList")
    public AppResponse<List<TMemberAddress>> findAddressList(String accessToken){
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (memberId == null || memberId.length() == 0){
            AppResponse response= AppResponse.fail(null);
            response.setMsg("当前用户非法登录");
            return  response;
        }
        //查询
        List<TMemberAddress> address = userService.findAddressByMemberId(Integer.parseInt(memberId));
        return AppResponse.ok(address);


    }
}
