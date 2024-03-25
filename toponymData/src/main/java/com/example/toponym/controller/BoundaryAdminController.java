package com.example.toponym.controller;

import com.example.toponym.model.BoundaryAdmin;
import com.example.toponym.model.PageBean;
import com.example.toponym.model.ResultResponse;
import com.example.toponym.service.BoundaryAdminService;
import com.github.pagehelper.PageHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name="基于行政区划的勘界统计")
@Slf4j
@RestController

@RequestMapping("/toponym/boundary")
public class BoundaryAdminController {
    @Autowired
    private BoundaryAdminService boundaryAdminService;
    @Operation(summary = "基于行政区划的面积、村级界线长度、村庄数量、边界点数量及边界长度统计")
    @GetMapping("/getadmin")
    public ResultResponse findAll(){
        List<BoundaryAdmin> list = boundaryAdminService.findAll();
        return ResultResponse.success(list);}

    @Operation(summary = "某一行政区的面积、村级界线长度、村庄数量、边界点数量及边界长度统计")
    @GetMapping("/getbyadmin")
    public ResultResponse findByAdmin(@Parameter(description = "行政区名，如八岭山镇、西城街道")String admin){
        List<Map<String,Object>> list=boundaryAdminService.findByAdmin(admin);
        return ResultResponse.success(list);
    }
}
