package com.offcn.user.excpetion;


import enums.UserExceptionEnum;

// 运行时异常 | 编译异常
public class UserException extends RuntimeException{
    public UserException(UserExceptionEnum userEnum){
        super(userEnum.getMsg());
    }
}
