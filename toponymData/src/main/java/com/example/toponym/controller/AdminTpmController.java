package com.example.toponym.controller;
import com.example.toponym.model.AdminTpm;
import com.example.toponym.model.DoorPlateAElement;
import com.example.toponym.model.Category;
import com.example.toponym.model.PageBean;
import com.example.toponym.model.ResultResponse;
import com.example.toponym.model.page.CommonInfoPage;
import com.example.toponym.service.AdminTpmService;
import com.github.pagehelper.PageHelper;
import com.google.gson.JsonObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name="基于行政区划的地名统计")
@Slf4j
@RestController

@RequestMapping("/toponym/admin")
public class   AdminTpmController {
    @Autowired
    private AdminTpmService adminTpmService;

    @Operation(summary = "统计表数据结构")
    @PostMapping("/admintpm")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "统计表数据结构",required = true)
    public AdminTpm show(@RequestBody AdminTpm adminTpm){
        return adminTpm;
    }
    @Operation(summary = "地名总数、点状地名数、线状地名数")
    @GetMapping("/getsum")
    public ResultResponse findSUM(){
        List<Map<String,Object>> list = adminTpmService.findSUM();
        return ResultResponse.success(list);}
    @Operation(summary = "统计表所有数据")
    @GetMapping("/getadmin")
    public ResultResponse findAll(){
        List<AdminTpm> list = adminTpmService.findAll();
        return ResultResponse.success(list);}

    @Operation(summary = "某一地名类别统计")
    @GetMapping("/getbyname")
    public ResultResponse findByName(@Parameter(description = "地名类别字段，参考AdminTpm对象，如STAT_WALL，STAT_POND",example = "STAT_POND")String category){
        List<Map<String,Object>> list = adminTpmService.findByName(category);
        return ResultResponse.success(list);}

    @Operation(summary = "某一行政区统计")
    @GetMapping("/getbyrank")
    public ResultResponse findByRank (@Parameter(description = "行政区名，如荆州区、西城街道")String admin) {
        List<Map<String,Object>> list=adminTpmService.findByRank(admin);
        return ResultResponse.success(list);
    }

//    @Operation(summary ="人口数量排行")
//    @GetMapping("/getbypop")
//    public ResultResponse findByPop(){
//        List<Map<String,Object>> list=adminTpmService.findByPop();
//        return ResultResponse.success(list);
//    }
    @Operation(summary ="地名大类数量统计")
    @GetMapping("/getbycategory")
    public ResultResponse findByCategory(){
        List<Map<String,Object>> list=adminTpmService.findByCategory();
        return ResultResponse.success(list);
    }

    @Operation(summary ="地名新增统计")
    @GetMapping("/getbyadd")
    public ResultResponse findByAdd(@Parameter(description = "开始年月，如2023-07")String start,@Parameter(description = "结束年月，如2023-08")String end){
        Map<String,Object> map=adminTpmService.findByAdd(start,end).get(0);
        com.alibaba.fastjson.JSON obj = com.alibaba.fastjson.JSON.parseObject((String) map.get("STAT_ADD"));
        map.replace("STAT_ADD",map.get("STAT_ADD"),obj);
        return ResultResponse.success(map);
    }

    @Operation(summary ="地名更新统计")
    @GetMapping("/getbyupdate")
    public ResultResponse findByUpdate(@Parameter(description = "开始年月，如2023-07")String start,@Parameter(description = "结束年月，如2023-08")String end){
        Map<String,Object> map=adminTpmService.findByUpdate(start,end).get(0);
        com.alibaba.fastjson.JSON obj = com.alibaba.fastjson.JSON.parseObject((String) map.get("STAT_UPDATE"));
        map.replace("STAT_UPDATE",map.get("STAT_UPDATE"),obj);
        return ResultResponse.success(map);
    }
}





