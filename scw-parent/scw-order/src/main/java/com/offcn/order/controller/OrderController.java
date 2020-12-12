package com.offcn.order.controller;


import com.offcn.common.response.AppResponse;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.vo.req.OrderInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/order")
public class OrderController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired(required = false)
    private OrderService orderService;

    @PostMapping("/createOrder")
    public AppResponse<TOrder> createOrder(@RequestBody OrderInfoVo orderInfoVo){
        // 获取当前用户的Id
        String accessToken = orderInfoVo.getAccessToken();
        String memeberId = redisTemplate.opsForValue().get(accessToken);
        if(memeberId == null){
            return AppResponse.fail(null);
        }
        try {
            TOrder tOrder = orderService.saveOrder(orderInfoVo);
            return AppResponse.ok(tOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail(null);
        }
    }

}
