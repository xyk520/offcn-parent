package com.offcn.order.vo.req;


import lombok.Data;

@Data
public class OrderInfoVo {

    private String accessToken;

    private Integer projectid; // 项目ID

    private Integer returnid;  // 回报ID

    private Integer rtncount;  /// 回报的数量

    private String address;  // 收货地址

    private String invoice;  // 是否开发票

    private String invoictitle;  // 发票头

    private String remark;  // 备注

}
