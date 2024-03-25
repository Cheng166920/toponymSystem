package com.example.toponym.controller;

import com.alibaba.fastjson.JSON;
import com.example.toponym.model.DoorPlateAElement;
import com.example.toponym.model.ResultResponse;
import com.example.toponym.model.Subject;
import com.example.toponym.model.ToponymHistory;
import com.example.toponym.service.SubjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name="地名专题统计")
@Slf4j
@RestController
@RequestMapping("/toponym/subject")
public class  SubjectController {
    @Autowired
    private SubjectService subjectService;
    private Subject subject;

    @Operation(summary = "统计表所有数据")
    @GetMapping("/getsubject")
    public ResultResponse findAll(){
        List<Map<String,Object>> list = subjectService.findAll();
        for (Map<String,Object> m:list) {
            JSONArray array = JSONArray.fromObject(m.get("STAT_CONTENT"));
            m.replace("STAT_CONTENT",m.get("STAT_CONTENT"),array);
        }
        return ResultResponse.success(list);
    }
    @Operation(summary = "某一地名类别统计数据")
    @GetMapping("/getbyname")
    public ResultResponse findBySUB( @Parameter(description = "地名类别，如单位、道路街巷")String subname){
        Map<String,Object> map=subjectService.findBySUB(subname).get(0);
        JSONArray array = JSONArray.fromObject(map.get("STAT_CONTENT"));
        map.replace("STAT_CONTENT",map.get("STAT_CONTENT"),array);
        return ResultResponse.success(map);
    }
}
