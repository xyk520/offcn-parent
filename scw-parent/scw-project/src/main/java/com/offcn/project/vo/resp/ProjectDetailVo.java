package com.offcn.project.vo.resp;


import com.offcn.project.pojo.TReturn;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProjectDetailVo implements Serializable {
    private Integer memberid;  // 发起人的Id

    private Integer id;  // 项目Id

    private String name;  // 项目名

    private String remark;  // 项目简介

    private Long money;  // 已经筹到的资金

    private Integer day;  // 众筹天数

    private String status; //0 - 即将开始， 1 - 众筹中， 2 - 众筹成功， 3 - 众筹失败

    private String deploydate;  // 发布日期

    private Long supportmoney;  // 支持金额

    private Integer supporter; // 支持者数量

    private Integer completion;  // 完成度

    private String createdate;   // 创建日期

    private Integer follower;  // 关注者数量

    // 图片
    // 头图
    private String headerImage;
    // 详情图
    private List<String> detailsImage;

    // 项目回报
    private List<TReturn> projectReturn;
}
