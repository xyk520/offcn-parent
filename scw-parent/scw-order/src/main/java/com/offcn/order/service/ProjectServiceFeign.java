package com.offcn.order.service;

import com.offcn.common.response.AppResponse;
import com.offcn.order.service.impl.ProjectServiceFeignImpl;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value="SCW-PROJECT",fallback = ProjectServiceFeignImpl.class)
public interface ProjectServiceFeign {

    @GetMapping("/project/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReturnListByPid(@PathVariable("projectId") Integer projectId);

    @GetMapping("/project/findReturnById/{returnId}")
    public TReturn findReturnById(@PathVariable("returnId") Integer returnId);

}
