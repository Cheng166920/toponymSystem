package com.example.toponym.controller;

import com.example.toponym.model.StatDoorPlate;
import com.example.toponym.model.ResultResponse;
import com.example.toponym.model.Subject;
import com.example.toponym.service.StatDoorPlateService;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name="地址统计")
@Slf4j
@RestController

@RequestMapping("/toponym/doorplate")
public class StatDoorPlateController {


    @Autowired
    private StatDoorPlateService statDoorPlateService;
    @Operation(summary = "门牌总数")
    @GetMapping("/getsum")
    public ResultResponse findSUM(){
        List<Map<String,Object>> list=statDoorPlateService.findSUM();
        return ResultResponse.success(list);}
    @Operation(summary = "所有道路门牌数量统计")
    @GetMapping("/getdoorplate")
    public ResultResponse findAll(){
        List<StatDoorPlate> list=statDoorPlateService.findAll();
        return ResultResponse.success(list);}

    @Operation(summary = "某一道路门牌数量")
    @GetMapping("/getbyroad")
    public  ResultResponse findByRoad(@Parameter(description = "道路名称，如荆州大道、九阳大道")String road){
        List<Map<String,Object>> list=statDoorPlateService.findByRoad(road);
        return ResultResponse.success(list);}

    @Operation(summary = "各道路所含门牌TOP5")
    @GetMapping("/gettopbyroad")
    public ResultResponse findRoad(){
        List<Map<String,Object>> list=statDoorPlateService.findRoad();
        return ResultResponse.success(list);}

    @Operation(summary = "各街镇所含门牌TOP5")
    @GetMapping("/gettopbytown")
    public ResultResponse findTown(){
        List<Map<String,Object>> list=statDoorPlateService.findTown();
        return ResultResponse.success(list);}

    @Operation(summary ="门牌新增统计")
    @GetMapping("/getbyadd")
    public ResultResponse findByAdd(@Parameter(description = "开始年月，如2023-07")String start,@Parameter(description = "结束年月，如2023-08")String end){
        Map<String,Object> map=statDoorPlateService.findByAdd(start,end).get(0);
        com.alibaba.fastjson.JSON obj = com.alibaba.fastjson.JSON.parseObject((String) map.get("STAT_ADD"));
        map.replace("STAT_ADD",map.get("STAT_ADD"),obj);
        return ResultResponse.success(map);
    }
    @Operation(summary ="门牌更新统计")
    @GetMapping("/getbyupdate")
    public ResultResponse findByUpdate(@Parameter(description = "开始年月，如2023-07")String start,@Parameter(description = "结束年月，如2023-08")String end){
        Map<String,Object> map=statDoorPlateService.findByUpdate(start,end).get(0);
        com.alibaba.fastjson.JSON obj = com.alibaba.fastjson.JSON.parseObject((String) map.get("STAT_UPDATE"));
        map.replace("STAT_UPDATE",map.get("STAT_UPDATE"),obj);
        return ResultResponse.success(map);
    }
}
