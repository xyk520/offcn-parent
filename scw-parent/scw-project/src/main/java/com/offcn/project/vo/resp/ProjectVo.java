package com.offcn.project.vo.resp;

import lombok.Data;

// 用来批量显示项目的信息
@Data
public class ProjectVo {
    // 会员的ID
    private Integer memberid;
     // 项目名
    private String name;
    // 项目ID
    private Integer id;
    // 项目简介
    private String remark;
    // 头图片
    private String  headerImage;
}
