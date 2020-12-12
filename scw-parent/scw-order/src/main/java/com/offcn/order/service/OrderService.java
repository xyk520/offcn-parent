package com.offcn.order.service;

import com.offcn.order.pojo.TOrder;
import com.offcn.order.vo.req.OrderInfoVo;

public interface OrderService {

    public TOrder saveOrder(OrderInfoVo infoVo);
}
