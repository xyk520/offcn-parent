package com.offcn.project.service;

import com.offcn.common.enums.ProjectStatusEnume;
import com.offcn.project.pojo.*;
import com.offcn.project.vo.req.ProjectRedisStoreVo;

import java.util.List;

public interface ProjectCreateService {

    //1 初始化项目
    public String initCreateProject(Integer memberId);

    // 4 保存项目
    public void saveProjectInfo(ProjectStatusEnume status, ProjectRedisStoreVo redisStoreVo);

    //展示所有项目
    public List<TProject> findAllProject();

    //根据项目id展示项目图片
    public List<TProjectImages> getProjectImagesByProjectId(Integer id);

    //根据项目的id获取项目的详细信息
    public TProject findProjectInfo(Integer projectId);

    //根据项目id获取回报数据
    public List<TReturn> findTReturnByProjectId(Integer id);

    //获取项目标签
    public List<TTag> findAllTag();

    //获取项目的所有分类
    public List<TType> findAllType();

    //根据回报id获取信息
    public TReturn findTReturnInfo(Integer returnId);
}
