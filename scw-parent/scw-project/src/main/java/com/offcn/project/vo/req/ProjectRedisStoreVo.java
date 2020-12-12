package com.offcn.project.vo.req;

import com.offcn.project.pojo.TReturn;
import lombok.Data;

import java.util.List;

// 在redis中存储的数据\
@Data
public class ProjectRedisStoreVo {
    // 第一步
    // 项目的临时 令牌 （作为redis中获取数据的key）
    private String  projectToken;
    private Integer memberid; // 会员Id

    // 第二步
    // project表中的数据
    private String name;  // 项目名称
    private String remark;  // 项目简介
    private Long money;  // 筹措的金额
    private Integer day;  // 天数
    private String createdate;  // 创建的时间
    // 头部图片 一张
    private String  headerImage;
    // 详情图片 多张
    private List<String> detailsImages;
    // 标签 多个
    private List<Integer> tagIds;
    // 类型 多个
    private List<Integer> typeIds;

    // 第三步
    // 回报数据
    private List<TReturn> projectReturns;

}
