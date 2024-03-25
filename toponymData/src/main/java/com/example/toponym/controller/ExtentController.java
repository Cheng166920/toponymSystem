package com.example.toponym.controller;

import com.example.toponym.common.BizException;
import com.example.toponym.model.ResultResponse;
import com.example.toponym.service.BoundaryService;
import com.example.toponym.service.ExtentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController

@Tag(name = "地图模块",description = "获取指定空间范围内的地名、界线和地址数据")
@RequestMapping("/toponym/extent")
public class ExtentController {
    @Autowired
    private ExtentService extentService;
    @Autowired
    private BoundaryService boundaryService;

    @Operation(summary = "点状地名数据")
    @GetMapping("/point")
    @Parameters({
            @Parameter(name = "polygon",description = "空间范围，组织为多边形坐标的形式，注意首尾坐标一致，例如POLYGON((112.26576805114746 30.457256514643596, 112.10380554199219 30.457256514643596, 112.10380554199219 30.39434753102366, 112.26576805114746 30.39434753102366, 112.26576805114746 30.457256514643596),(112.22049236297609 30.44571390562601, 112.22049236297609 30.414260761324122, 112.13951110839845 30.414260761324122, 112.13951110839845 30.44571390562601, 112.22049236297609 30.44571390562601))"),
            @Parameter(name = "scale",description = "缩放级别",example = "12")
    })
    public ResultResponse findPointByExtent(String polygon,Integer scale){
        com.alibaba.fastjson.JSONObject obj =  extentService.findPointByExtent(polygon,scale);
        return ResultResponse.success(obj);
    }

    @Operation(summary = "地址数据")
    @GetMapping("/doorplate")
    public ResultResponse findByDoorplateExtent(@Parameter(description = "空间范围，组织为多边形坐标的形式，注意首尾坐标一致，例如POLYGON((112.20576805114746 30.367256514643596, 112.10380554199219 30.457256514643596, 112.10380554199219 30.39434753102366, 112.26576805114746 30.39434753102366, 112.20576805114746 30.367256514643596),(112.16049236297609 30.38571390562601, 112.22049236297609 30.414260761324122, 112.13951110839845 30.414260761324122, 112.13951110839845 30.44571390562601, 112.16049236297609 30.38571390562601))")String polygon){
        com.alibaba.fastjson.JSON obj = extentService.findDoorplateByExtent(polygon);
        return ResultResponse.success(obj); }

    @Operation(summary = "线状地名数据")
    @GetMapping("/line")
    @Parameters({
            @Parameter(name = "polygon",description = "空间范围，组织为多边形坐标的形式，注意首尾坐标一致，例如POLYGON((112.26576805114746 30.457256514643596, 112.10380554199219 30.457256514643596, 112.10380554199219 30.39434753102366, 112.26576805114746 30.39434753102366, 112.26576805114746 30.457256514643596),(112.22049236297609 30.44571390562601, 112.22049236297609 30.414260761324122, 112.13951110839845 30.414260761324122, 112.13951110839845 30.44571390562601, 112.22049236297609 30.44571390562601))"),
            @Parameter(name = "scale",description = "缩放级别",example = "12")
    })
    public ResultResponse findLineByExtent(String polygon,Integer scale){
        com.alibaba.fastjson.JSONObject obj = extentService.findLineByExtent(polygon,scale);
        return ResultResponse.success(obj); }

    @Operation(summary = "地名标志数据")
    @GetMapping("/sign")
    public ResultResponse findSign(){
        com.alibaba.fastjson.JSONObject obj = extentService.findSign();
        return ResultResponse.success(obj);
    }

    @Operation(summary = "界线数据")
    @GetMapping(value = "/bd_line")
    public ResultResponse findBoundary(@Parameter(description = "简化级别，阈值：0km、0.01km、0.02km、0.05km、0.1km。0km表示未简化，",required = true)String level){
        JSONObject obj = boundaryService.findBoundary(level);
        return ResultResponse.success(obj);
    }

    @Operation(summary = "界桩数据")
    @GetMapping("/stake")
    public ResultResponse findStake(){
        JSONObject obj = boundaryService.findBoundaryStake();
        return ResultResponse.success(obj);
    }

    @Operation(summary = "边界点数据")
    @GetMapping("/bd_point")
    public ResultResponse findPoint(){
        JSONObject obj = boundaryService.findBoundaryPoint();
        return ResultResponse.success(obj);
    }

    @Operation(summary = "三交点数据")
    @GetMapping("/triple_node")
    public ResultResponse findTripleNode(){
        JSONObject obj = boundaryService.findTripleNode();
        return ResultResponse.success(obj);
    }

//    @GetMapping("/scale")
//    public List<String> findCategory(Integer scale){
//        return pointExtentService.findCategory(scale);
//    }
}
