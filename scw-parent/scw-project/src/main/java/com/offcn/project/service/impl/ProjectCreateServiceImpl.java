package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.netflix.discovery.converters.Auto;
import com.offcn.common.enums.ProjectStatusEnume;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisStoreVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {


    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired(required = false)
    private TProjectMapper projectMapper;

    @Autowired(required = false)
    private TProjectTagMapper tagMapper;

    @Autowired(required = false)
    private TProjectTypeMapper typeMapper;

    @Autowired(required = false)
    private TProjectImagesMapper imagesMapper;

    @Autowired(required = false)
    private TReturnMapper returnMapper;

    @Autowired(required = false)
    private TTagMapper tTagMapper;

    @Autowired(required = false)
    private TTypeMapper tTypeMapper;

    @Override
    public String initCreateProject(Integer memberId) {
        //为项目创建一个 临时令牌token 方便后续进行存取的操作
        String projectToken = UUID.randomUUID().toString().replace("-", "") + "_project";
        //创建一个projectredisstrorevo的空对象
        ProjectRedisStoreVo redisVo = new ProjectRedisStoreVo();
        //将memberid加入到初始化的项目对象中
        redisVo.setMemberid(memberId);
        //降临时的token引入到Redis中
        String redisVoStr = JSON.toJSONString(redisVo);
        redisTemplate.opsForValue().set(projectToken, redisVoStr);

        return projectToken;
    }

    @Override
    public void saveProjectInfo(ProjectStatusEnume status, ProjectRedisStoreVo redisStoreVo) {
        TProject tProject = new TProject();
        BeanUtils.copyProperties(redisStoreVo, tProject);
        tProject.setStatus(status.getCode() + "");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tProject.setCreatedate(dateFormat.format(new Date()));
        // 插入数据到mysql中
        projectMapper.insert(tProject);
        // 获取刚刚插入数据的Id
        Integer projectId = tProject.getId();
        // 插入头图片
        String headerImage = redisStoreVo.getHeaderImage();
        if (headerImage != null) {
            TProjectImages tProjectImages = new TProjectImages(null, projectId, headerImage, ProjectImageTypeEnume.HEADER.getCode());
            imagesMapper.insert(tProjectImages);
        }
        // 详情图片
        List<String> detailsImages = redisStoreVo.getDetailsImages();
        if (detailsImages != null && detailsImages.size() > 0) {
            for (String detailImage : detailsImages) {
                TProjectImages detailImageObj = new TProjectImages(null, projectId, detailImage, ProjectImageTypeEnume.DETAILS.getCode());
                imagesMapper.insert(detailImageObj);
            }
        }

        // 标签信息
        List<Integer> tagIds = redisStoreVo.getTagIds();
        if (tagIds != null && tagIds.size() > 0) {
            for (Integer tagId : tagIds) {
                TProjectTag tProjectTag = new TProjectTag(null, projectId, tagId);
                tagMapper.insert(tProjectTag);
            }
        }

        // 分类信息
        List<Integer> typeIds = redisStoreVo.getTypeIds();
        if (typeIds != null && typeIds.size() > 0) {
            for (Integer typeId : typeIds) {
                TProjectType tProjectType = new TProjectType(null, projectId, typeId);
                typeMapper.insert(tProjectType);
            }
        }

        // 回报数据
        List<TReturn> projectReturns = redisStoreVo.getProjectReturns();
        if (projectReturns != null && projectReturns.size() > 0) {
            for (TReturn tReturn : projectReturns) {
                tReturn.setProjectid(projectId);
                returnMapper.insert(tReturn);
            }
        }

        // 清空redis
//         redisTemplate.delete(redisStoreVo.getProjectToken());

    }

    @Override
    public List<TProject> findAllProject() {
        return projectMapper.selectByExample(null);
    }

    @Override
    public List<TProjectImages> getProjectImagesByProjectId(Integer id) {
        TProjectImagesExample example = new TProjectImagesExample();
        TProjectImagesExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(id);
        return imagesMapper.selectByExample(example);
    }

    @Override
    public TProject findProjectInfo(Integer projectId) {
        return  projectMapper.selectByPrimaryKey(projectId);
    }

    @Override
    public List<TReturn> findTReturnByProjectId(Integer id) {
        TReturnExample example = new TReturnExample();
        TReturnExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(id);
        return returnMapper.selectByExample(example);
    }

    @Override
    public List<TTag> findAllTag() {
        return tTagMapper.selectByExample(null);
    }

    @Override
    public List<TType> findAllType() {
        return tTypeMapper.selectByExample(null);
    }

    @Override
    public TReturn findTReturnInfo(Integer returnId) {
        return returnMapper.selectByPrimaryKey(returnId);
    }
}
