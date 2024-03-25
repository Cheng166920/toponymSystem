package com.example.toponym.controller;

import com.example.toponym.model.PageBean;
import com.example.toponym.model.ResultResponse;
import com.example.toponym.service.BoundaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.Map;


@Tag(name = "界线管理模块",description = "界线检索和界线申领")
@Slf4j
@RestController
@RequestMapping("/boundary")
@CrossOrigin
public class BoundaryController {
    @Autowired
    private BoundaryService boundaryService;

    @Operation(summary = "根据标识码获取界线详细信息")
    @GetMapping("/detail")
    public ResultResponse findByCode(@Parameter(description = "标识码",example = "66b954e1-689d-40f5-a100-1eed8113ca60")String code){
        Map<String,Object> map = boundaryService.findByCode(code);
        return ResultResponse.success(map);
    }


    @Operation(summary = "1.用户发起界线申领请求")
    @GetMapping("/downloadRequest")
    public ResultResponse downloadRequest(@Parameter(description = "标识码",example = "66b954e1-689d-40f5-a100-1eed8113ca60")String code,@Parameter(description = "用户名")String user){
        Map<String,Object> map = boundaryService.downloadRequest(code,user);
        return ResultResponse.success(map);
    }

    @Operation(summary = "2.审核员查看界线申领请求")
    @GetMapping("/getRequest")
    public ResultResponse getRequest(){
        List<Map<String,Object>> lists = boundaryService.findAllRequest();
        return ResultResponse.success(lists);
    }

    @Operation(summary = "3.审核员审批界线申领请求")
    @GetMapping("/review")
    public ResultResponse downloadResponse(@Parameter(description = "序号")Integer gid, @Parameter(description = "界线审批，阈值：审核通过，审核不通过",example = "审核通过")String review){
        Map<String,Object> list = boundaryService.downloadReview(gid,review);
        return ResultResponse.success(list);
    }

    @Operation(summary = "审核员删除界线申领请求")
    @GetMapping("/delete")
    public ResultResponse DeleteResponse(@Parameter(description = "序号")Integer gid){
        boundaryService.deleteRequest(gid);
        return ResultResponse.success();
    }

    @Operation(summary = "4.用户申领界线数据",description = "通过审核后，提供界线数据下载接口，下载的数据为shp文件，压缩包（.zip）包含.dbf、.prj、.shp、.shx和.fix五个文件")
    @GetMapping("/download")
    public ResultResponse downloadShp(@Parameter(description = "序号")Integer gid,HttpServletResponse response) throws Exception {
        boundaryService.downloadShp(gid,response);
        return ResultResponse.success();
    }

    @Operation(summary = "勘界成果图表下载",description = "包括成果图、成果表、协议书")
    @GetMapping("/file")
    public ResultResponse getFile(@Parameter(description = "标识码",example = "afb52c80-5462-4734-aa23-d43a77f07c56")String code, @Parameter(description = "成果类型，阈值：成果图、边界点成果表、协议书、协议书附图",example = "协议书")String fileType,HttpServletResponse response) throws Exception {
        byte[] bytes = boundaryService.getFile(code,fileType,response);
        return ResultResponse.success(bytes);
    }

    @Operation(summary = "界线管理",description = "检索勘界类型，查看界线界桩的属性信息和纸质类材料")
    @GetMapping ("/info")
    public ResultResponse findInfo(@Parameter(description = "勘界类型，阈值：边界点、三交点、县级界桩、市级界线、县级界线、乡级界线、村级管理线",example = "边界点")String type){
        List<Map<String,Object>> lists = boundaryService.findInfo(type);
        return ResultResponse.success(lists);
    }

    @Operation(summary = "界线检索",description = "检索界线名称，查看界线的属性信息")
    @Parameters({
            @Parameter(name = "name",description = "界线名称",example = "东城"),
            @Parameter(name = "currentPage",description = "当前页",example = "1"),
            @Parameter(name = "pageSize",description = "页面大小",example = "20")
    })
    @GetMapping ("/boundaryName")
    public ResultResponse findByName(String name,Integer currentPage,Integer pageSize){
        PageBean<Map<String, Object>> lists = boundaryService.findByName(name,currentPage,pageSize);
        return ResultResponse.success(lists);
    }

}
