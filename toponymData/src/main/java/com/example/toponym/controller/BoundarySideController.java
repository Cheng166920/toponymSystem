package com.example.toponym.controller;
import com.example.toponym.model.BoundarySide;
import com.example.toponym.model.ResultResponse;
import com.example.toponym.service.BoundarySideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="相邻行政区划的共边统计")
@Slf4j
@RestController

@RequestMapping("/toponym/boundary")
public class BoundarySideController {
    @Autowired
    private BoundarySideService boundarySideService;
    @Operation(summary = "两个相邻行政区间界线长度统计")
    @GetMapping("/getcoside")
    public ResultResponse findAll(){
        List<BoundarySide> list=boundarySideService.findAll();
        return ResultResponse.success(list);}
}
