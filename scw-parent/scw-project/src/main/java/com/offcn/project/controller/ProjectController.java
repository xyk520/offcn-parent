package com.offcn.project.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.common.utils.OSSTemplate;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.resp.ProjectDetailVo;
import com.offcn.project.vo.resp.ProjectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
@Api(tags = "项目模块")
public class ProjectController {

    @Autowired
    private OSSTemplate ossTemplate;

    @Autowired
    private ProjectCreateService projectCreateService;


    @PostMapping("/upload")
    public AppResponse<Map<String,Object>> uploadFile(@RequestParam("file")MultipartFile[] files) throws IOException {
        Map<String,Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        if (files != null && files.length > 0){
            for (MultipartFile item : files) {
                //获取文件的文件流 item.getinputStrem
                //获取文件之前的名字 item.getoriginalfilename
                String fileUrl = ossTemplate.upload(item.getInputStream(), item.getOriginalFilename());
                list.add(fileUrl);
            }
        }
        map.put("urls",list);
        return AppResponse.ok(map);
    }

    @ApiOperation(value ="展示项目")
    @GetMapping("/all")
    public AppResponse<List<ProjectVo>> findAllProject(){
        //1 创建集合用于存储所有的项目
        List<ProjectVo> list = new ArrayList<>();
        //2 查询
        List<TProject> prosVos = projectCreateService.findAllProject();
        //3 当前的内容是他tproject 而返回值类型是projectVo
        for (TProject tProject : prosVos) {
            //获取当前项目名
            Integer projectId = tProject.getId();
            //根据项目id查询头图片
            List<TProjectImages> images = projectCreateService.getProjectImagesByProjectId(projectId);
            ProjectVo projectVo = new ProjectVo();
            //赋值属性
            BeanUtils.copyProperties(tProject, projectVo);
            //还少头图片添加头图片
            if (images !=null && images.size() > 0){
            for (TProjectImages image : images) {
                //获取头图片
                if (image.getImgtype() == 0) {
                    projectVo.setHeaderImage(image.getImgurl());
                }
              }
            }
            list.add(projectVo);
        }

        return AppResponse.ok(list);
    }
    @ApiOperation(value ="查询项目的数据")
    @GetMapping("/findProjectInfo/{projectId}")
    public AppResponse<ProjectDetailVo> findProjectInfo(@PathVariable("projectId") Integer projectId){
        ProjectDetailVo detailVo = new ProjectDetailVo();
        TProject projectInfo = projectCreateService.findProjectInfo(projectId);
        BeanUtils.copyProperties(projectInfo, detailVo);
        //图片的添加
        detailVo.setDetailsImage(new ArrayList<>());
        List<TProjectImages> images = projectCreateService.getProjectImagesByProjectId(projectId);
        if (images == null){
            detailVo.setHeaderImage(null);
        }else {
            for (TProjectImages image : images) {
                if (image.getImgtype() == 0){
                    detailVo.setHeaderImage(image.getImgurl());
                }else {
                    detailVo.getDetailsImage().add(image.getImgurl());
                }
            }
        }
        //回报的添加
        List<TReturn> returns = projectCreateService.findTReturnByProjectId(projectId);
        detailVo.setProjectReturn(returns);
        return AppResponse.ok(detailVo);
    }

    @ApiOperation(value ="查询所有的标签")
    @GetMapping("/findAllTag")
    public AppResponse<List<TTag>> findAllTag(){
        List<TTag> tags = projectCreateService.findAllTag();
        return AppResponse.ok(tags);
    }

    @ApiOperation(value ="查询所有的分类")
    @GetMapping("/findAllType")
    public AppResponse<List<TType>> findAllType(){
        List<TType> allType = projectCreateService.findAllType();
        return AppResponse.ok(allType);
    }

    @ApiOperation(value ="根据回报id获取信息")
    @GetMapping("/findTReturnInfo/{returnId}")
    public TReturn findTReturnInfo(@PathVariable("returnId7 ") Integer returnId){
        return projectCreateService.findTReturnInfo(returnId);
    }

    @ApiOperation(value ="查询所有的===")
    @GetMapping("//details/returns/{projectId}")
    public AppResponse<List<TReturn>> findReturnById(@PathVariable("projectId") Integer projectId){
        List<TReturn> returns = projectCreateService.findTReturnByProjectId(projectId);
        return AppResponse.ok(returns);
    }

}
