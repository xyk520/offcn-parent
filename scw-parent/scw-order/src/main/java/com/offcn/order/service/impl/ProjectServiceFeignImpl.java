package com.offcn.order.service.impl;

import com.offcn.common.response.AppResponse;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectServiceFeignImpl implements ProjectServiceFeign {

    // 服务降级
    @Override
    public AppResponse<List<TReturn>> getReturnListByPid(Integer projectId) {
        AppResponse<List<TReturn>> fail = AppResponse.fail(null);
        fail.setMsg("远程调用失败[订单]");
        return fail;
    }

    @Override
    public TReturn findReturnById(Integer returnId) {
        return new TReturn();
    }
}
