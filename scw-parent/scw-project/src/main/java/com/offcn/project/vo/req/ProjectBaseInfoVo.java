package com.offcn.project.vo.req;

import com.offcn.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class ProjectBaseInfoVo extends BaseVo {

    @ApiModelProperty(value = "项目的临时token")
    private String  projectToken;  // 项目的临时token
    @ApiModelProperty(value = "项目名称")
    private String name;  // 项目名称
    @ApiModelProperty(value = "项目简介")
    private String remark;  // 项目简介
    @ApiModelProperty(value = "筹措的金额")
    private Long money;  // 筹措的金额
    @ApiModelProperty(value = "天数")
    private Integer day;  // 天数
    @ApiModelProperty(value = "头图片")
    private String  headerImage;
    // 详情图片 多张
    @ApiModelProperty(value = "详情图片")
    private List<String> detailsImages;
    // 标签 多个
    @ApiModelProperty(value = "标签(可多选)")
    private List<Integer> tagIds;
    // 类型 多个
    @ApiModelProperty(value = "标签(可多选)")
    private List<Integer> typeIds;
}
