package com.offcn.order.service.impl;

import com.offcn.common.enums.OrderStatusEnumes;
import com.offcn.common.response.AppResponse;
import com.offcn.common.utils.AppDateUtils;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoVo;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProjectServiceFeign projectService;

    @Override
    public TOrder saveOrder(OrderInfoVo infoVo) {
        TOrder order = new TOrder();
        // accessToken 转为 用户的Id 存储到order中
        String memberId =(String) redisTemplate.opsForValue().get(infoVo.getAccessToken());
        order.setMemberid(Integer.parseInt(memberId));
        // 将infoVo用户提交的数据复制到 order对象中
        BeanUtils.copyProperties(infoVo,order);
        // 生成订单编号
        String orderNum = UUID.randomUUID().toString().replace("-", "");
        order.setOrdernum(orderNum);
        // 创建时间
        order.setCreatedate(AppDateUtils.getFormartTime());
        // 订单状态
        order.setStatus(OrderStatusEnumes.UNPAY.getCode() + "");
        // money   回报个数 order.getRtncount() * 支持金额  + 运费
//        AppResponse<List<TReturn>> returnsResp = projectService.getReturnListByPid(infoVo.getProjectid());
//        List<TReturn> returns = returnsResp.getData();
//        // 默认获取第一个回报
//        //TReturn tReturn = returns.get(0);
//        // 保证 returnId 是属于  projectId 的回拨
//        for(TReturn tReturn : returns){
//            if(tReturn.getId().equals(infoVo.getReturnid())){
//                Integer money = order.getRtncount() * tReturn.getSupportmoney() + tReturn.getFreight();
//                order.setMoney(money);
//            }
//        }
        // 直接通过returnId 获取数据  没有验证 当前 returnId 是否是属于 projectId对应的回报
        TReturn tReturn = projectService.findReturnById(infoVo.getReturnid());
        Integer money = order.getRtncount() * tReturn.getSupportmoney() + tReturn.getFreight();
        order.setMoney(money);

        return order;
    }
}
