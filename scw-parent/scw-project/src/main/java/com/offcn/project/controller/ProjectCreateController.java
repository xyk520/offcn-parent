package com.offcn.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.common.enums.ProjectStatusEnume;
import com.offcn.common.response.AppResponse;
import com.offcn.common.vo.BaseVo;
import com.offcn.project.pojo.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectBaseInfoVo;
import com.offcn.project.vo.req.ProjectRedisStoreVo;
import com.offcn.project.vo.req.ProjectReturnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "新建项目的四个步骤")
@RestController
@RequestMapping("/createProject")
public class ProjectCreateController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectCreateService projectCreateService;

    @ApiOperation("项目新建的第一步：初始化项目")
    @RequestMapping("/init")
    public AppResponse<String> init(BaseVo baseVo){
        //可以通过basevo中的用户令牌获取用户的信息
        String accessToken = baseVo.getAccessToken();
        //从Redis中获取memberID
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (memberId == null || memberId.length() == 0) {
           AppResponse appResponse = AppResponse.fail(null);
           appResponse.setMsg("没有该用户，请先注册");
           return appResponse;
        }
        //通过数据库获取member信息
        String projectToken = projectCreateService.initCreateProject(Integer.parseInt(memberId));

        return AppResponse.ok(projectToken);
    }

    @ApiOperation("项目新建的第二步:添加项目的基本信息")
    @PostMapping("/saveBaseInfo")
    public AppResponse<String> saveBaseInfo(ProjectBaseInfoVo infoVo){
        // 从redis中获取第一步存入的对象
        String projectToken = infoVo.getProjectToken();
        // 从redis中获取的String类型的JSON值
        String redisVoStr = redisTemplate.opsForValue().get(projectToken);
        // String类型的JSON值转为对象
        ProjectRedisStoreVo redisVo = JSON.parseObject(redisVoStr, ProjectRedisStoreVo.class);
        // 将参数InfoVo对象中的数据 复制 到 redisVo 中 完成基本数据的添加
        BeanUtils.copyProperties(infoVo,redisVo);
        // 将添加完基本数据的 redisVo 写回到redis中
        redisVoStr = JSON.toJSONString(redisVo);
        redisTemplate.opsForValue().set(projectToken,redisVoStr);
        return AppResponse.ok(projectToken);
    }

    @ApiOperation("项目新建的第三步:添加项目的回报信息")
    @PostMapping("/saveReturn")
    public AppResponse<String> saveReturn(@RequestBody List<ProjectReturnVo> returnVoList){
        // 从参数中获取项目的临时令牌
        if(returnVoList != null && returnVoList.size() > 0){
            // 获取第一个元素中的 项目临时令牌
            String projectToken = returnVoList.get(0).getProjectToken();
            // 通过项目令牌获取项目数据
            String redisVoStr = redisTemplate.opsForValue().get(projectToken);
            // 将redisStr转换为 ProjectRedisStoreVo
            ProjectRedisStoreVo redisVo = JSON.parseObject(redisVoStr, ProjectRedisStoreVo.class);
            // 将页面收集的数据加入到 redisVo 中
            // 将 参数returnVoList 中的数据 倒腾到 List<TReturn>
            List tReturns = new ArrayList();
            for(ProjectReturnVo returnVo : returnVoList){
                TReturn tReturn = new TReturn();
                BeanUtils.copyProperties(returnVo,tReturn);
                tReturns.add(tReturn);
            }
            // 添加到redisVo
            redisVo.setProjectReturns(tReturns);
            // 将加入完成的数据写入到redis中
            redisVoStr = JSON.toJSONString(redisVo);
            redisTemplate.opsForValue().set(projectToken,redisVoStr);
            return AppResponse.ok(projectToken);
        }
        return AppResponse.fail("请输入参数");

    }

    @ApiOperation("项目新建的第四步:添加项目到mysql数据库")
    @PostMapping("/saveToMysql")
    public AppResponse<Object> saveToMysql(String projectToken,String accessToken,String status){
        // 1. 获取当前用户信息
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if(memberId == null || memberId.length() == 0){
            return AppResponse.fail("当前用户没有登录，请登录之后再尝试");
        }
        // 2. 获取前三步完成的项目
        String redisStrVo = redisTemplate.opsForValue().get(projectToken);
        ProjectRedisStoreVo redisVo = JSON.parseObject(redisStrVo, ProjectRedisStoreVo.class);
        // 3. 判断数据非空
        if(redisVo != null ){
            if(status.equalsIgnoreCase("1")) {
                // 获取添加的操作
                projectCreateService.saveProjectInfo(ProjectStatusEnume.SUBMIT_AUTH, redisVo);
                return AppResponse.ok("添加成功");
            }
        }
        return AppResponse.fail(null);
    }
}
