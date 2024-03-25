package com.example.toponym.controller;

import com.alibaba.fastjson.TypeReference;
import com.example.toponym.model.*;
import com.example.toponym.service.DoorPlateService;
import com.github.pagehelper.PageHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@Tag(name = "门牌检索模块",description = "包括门牌分类检索和门牌模糊检索，检索对象为门牌应用数据库中的所有数据")

@RequestMapping("/doorplate")

public class DoorPlateController {

    @Autowired
    private DoorPlateService doorPlateService;

    @Operation(summary = "模糊检索")
    @GetMapping("/fts_query")
    @Parameters({
            @Parameter(name = "query",description = "需要模糊检索的文本，如：河套社区复兴大道"),
            @Parameter(name = "currentPage",description = "当前页"),
            @Parameter(name = "pageSize",description = "页面大小")
    })
    public ResultResponse ftsQuery(String query, @RequestParam(name = "currentPage",defaultValue = "1")int currentPage,
                                   @RequestParam(name = "pageSize",defaultValue = "20")int pageSize){
        List<DoorPlateElement> doorPlateElementList = doorPlateService.selectFts(query,currentPage,pageSize);
        PageBean<DoorPlateElement> pageBean = new PageBean<>(doorPlateElementList);
        return ResultResponse.success(pageBean);
    }

    @Operation(summary = "分类检索")
    @GetMapping("/cat_query")
    @Parameters({
            @Parameter(name = "field",description = "需要检索的字段，可选参数为【uid、province、city、district、township、village、road、road_direction、estate、building、unit、doorplate、doorplate_sub、room、house_license、license_time、category、owner、owner_phone、construction、principal、principal_phone、collect_time、linear_toponym、point_toponym、geom、remarks】"),
            @Parameter(name = "value",description = "需要检索的值，根据不同字段执行get_field接口"),
            @Parameter(name = "currentPage",description = "当前页"),
            @Parameter(name = "pageSize",description = "页面大小")
    })
    public ResultResponse catQuery(String field, String value, @RequestParam(name = "currentPage",defaultValue = "1")int currentPage,
                                               @RequestParam(name = "pageSize",defaultValue = "20")int pageSize){
        PageHelper.startPage(currentPage,pageSize);
        List<DoorPlateElement> doorPlateElementList = doorPlateService.catQuery(field,value);
        return ResultResponse.success(new PageBean<>(doorPlateElementList));
    }

    @Operation(summary = "uid检索")
    @GetMapping("/findById")
    public ResultResponse findById(@Parameter(description = "需要检索的uid，如：4210031062041400002")String uid){
        List<DoorPlateElement> doorPlateElementList = doorPlateService.findById(uid);
        return ResultResponse.success(doorPlateElementList.get(0));
    }

    @Operation(summary = "获取某字段下所有的属性值")
    @GetMapping("/get_field")
    @Parameters({
            @Parameter(name = "field",description = "需要检索的字段，可选参数为【uid、province、city、district、township、village、road、road_direction、estate、building、unit、doorplate、doorplate_sub、room、house_license、license_time、category、owner、owner_phone、construction、principal、principal_phone、collect_time、linear_toponym、point_toponym、geom、remarks】"),
    })
    public ResultResponse getFiledValue(String field){
        JSONObject mapList = doorPlateService.getFiledValue(field);
        return ResultResponse.success(mapList);
    }

//    @Operation(summary = "分词接口")
//    @GetMapping("/segment")
//    public ResultResponse segment(@Parameter(description = "需要分词的文本，如：荆州市荆州区西环路129-148号")String query){
//        return ResultResponse.success(doorPlateService.segment(query));
//    }

    @Operation(summary = "历史变更回溯")
    @GetMapping("/history")
    @Parameters({
            @Parameter(name = "uid",description = "需要检索的uid，如：4210031062061400001")
    })
    public ResultResponse findHistoryByUID(String uid){
        List<DoorPlateHistory> doorPlateHistoryList = doorPlateService.findHistoryByUID(uid);
        return ResultResponse.success(doorPlateHistoryList);
    }

    @Operation(summary = "查看历史变更回溯详细信息")
    @GetMapping("/historyByDetail")
    @Parameters({
            @Parameter(name = "uid",description = "需要检索的uid，如：4210031062061400001"),
            @Parameter(name = "change_time",description = "需要检索的时间，如：2023-07-04 19:33:55, 该参数为空时默认查询uid下所有值")
    })
    public ResultResponse findHistoryByUID(String uid,@RequestParam(name = "change_time", required = false) String change_time){
        List<DoorPlateRecord> doorPlateRecordList= doorPlateService.findHistoryDetail(uid,change_time);
        return ResultResponse.success(doorPlateRecordList);
    }

    @Operation(summary = "门牌登记表")
    @GetMapping("/register")
    public ResultResponse getRegisterTable(@Parameter(description = "标识码uid,例如：4210031062041400002")String uid, HttpServletResponse response) throws Exception{
        Map<String, String> map;
        DoorPlateElement doorPlateElement = doorPlateService.findById(uid).get(0);
        map = com.alibaba.fastjson.JSON.parseObject(com.alibaba.fastjson.JSON.toJSONString(doorPlateElement), new TypeReference<Map<String, String>>() {});
        doorPlateService.getRegisterTable(map,response);
        return ResultResponse.success();
    }


}
