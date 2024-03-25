package com.example.toponym.controller;
import com.example.toponym.model.ResultResponse;
import com.example.toponym.model.BoundaryCategory;
import com.example.toponym.service.BoundaryCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@Tag(name="基于行政等级的勘界统计")
@Slf4j
@RestController

@RequestMapping("/toponym/boundary")
public class BoundaryCategoryController {
    @Autowired

    private BoundaryCategoryService boundaryService;
    @Operation(summary = "界线长度、行政区划个数、界线条数、三角点数量、边界点数量、界桩总数")
    @GetMapping("/getsum")
    public ResultResponse findSUM(){
        List<Map<String,Object>> list=boundaryService.findSUM();
        return ResultResponse.success(list);
    }
    @Operation(summary = "基于行政级别的界桩数量、界线长度、三交点数量及边界点数量统计")
    @GetMapping("/getboundary")
    public ResultResponse findAll(){
        List<BoundaryCategory> list=boundaryService.findAll();
        return ResultResponse.success(list);
    }
    @Operation(summary = "某一行政级别的界桩数量、界线长度、三交点数量及边界点数量统计")
    @GetMapping("/getbyrank")
    public  ResultResponse findByRank(@Parameter(description = "行政级别，包括市级、县级、乡级、村级")String rank){
        List<Map<String,Object>> list=boundaryService.findByRank(rank);
        return ResultResponse.success(list);}
}
