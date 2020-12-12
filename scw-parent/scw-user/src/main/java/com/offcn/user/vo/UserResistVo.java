package com.offcn.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// vo 就是 view object  视图对象 在视图中显示的对象
@ApiModel(value = "用户注册的视图对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResistVo {

    @ApiModelProperty("手机号")
    private String loginacct;
    @ApiModelProperty("密码")
    private String userpswd;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("验证码")
    private String code;
    @ApiModelProperty("用户名")
    private String username;
}
