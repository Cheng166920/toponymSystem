package com.example.toponym.controller;

import com.example.toponym.model.CommonInfo;
import com.example.toponym.model.PageBean;
import com.example.toponym.model.ResultResponse;
import com.example.toponym.model.page.CommonInfoPage;
import com.example.toponym.service.CommonInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController

@Tag(name = "地名检索模块",description = "包括地名分类检索和地名模糊检索，检索对象为地名应用数据库中的所有数据")
@RequestMapping("/toponym/info")
public class CommonInfoController {
    @Autowired
    private CommonInfoService commonInfoService;

    @Operation(summary = "模糊检索")
    @GetMapping("/fts_query")
    @Parameters({
            @Parameter(name = "currentPage",description = "当前页",example = "1"),
            @Parameter(name = "pageSize",description = "页面大小",example = "20"),
            @Parameter(name = "query",description = "地名相关属性",example = "庙湖")
    })
    public ResultResponse ftsQuery(String query,Integer currentPage,Integer pageSize){
        List<Map<String, Object>> list = commonInfoService.ftsQuery(query,currentPage,pageSize);
        PageBean<Map<String, Object>> pageBean = new PageBean<>(list);
        return ResultResponse.success(pageBean);
    }

    @Operation(summary = "获取指定目标的详细信息")
    @GetMapping ("/code")
    public ResultResponse findByCode(@Parameter(description = "标识码",example = "0020500d-492e-493b-878e-6a5758f86251")String code){
        Map<String, String> map = commonInfoService.findByIDENTIFICATION_CODE(code);
        return ResultResponse.success(map);
    }

    //返回部分字段（元数据表中配置）
    @Operation(summary = "分类检索")
    @PostMapping("/category_page")
    @Parameters({
            @Parameter(name = "currentPage",description = "当前页",example = "1"),
            @Parameter(name = "pageSize",description = "页面大小",example = "20"),
            @Parameter(name = "DIAGRAM_NAME",description = "检索字段：图上名称",example = "庙湖"),
            @Parameter(name = "TOPONYMIC_CATEGORY",description = "检索字段：地名类别，可选类别参考接口http://localhost:8080/toponym/info/all_category提供的所有地名类别。例如陆地水系、河流、湖泊等",example = "湖泊"),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "地名应用数据表分页对象结构，请输入地名应用数据库字段及其对应的值和分页参数，当该字段无需检索时，不需要输入该字段及其对应的值。",required = true)
    public ResultResponse findInfoPage(@RequestBody CommonInfoPage commonInfoPage){
        PageBean<Map<String, Object>> pageBean = commonInfoService.findInfoPage(commonInfoPage);
        return ResultResponse.success(pageBean);
    }

    @Operation(summary = "查看所有地名类型")
    @GetMapping("/all_category")
    public ResultResponse getCategoryTree(){
        List<Map<String,Object>> list = commonInfoService.getCategoryTree();
        return ResultResponse.success(list);
    }

    @Operation(summary = "地名登记表")
    @GetMapping("/register")
    public ResultResponse getRegisterTable(@Parameter(description = "标识码",example = "00251cc9-edbd-4010-a03f-4012bf39d6e0")String code, HttpServletResponse response) throws Exception{
        Map<String, String> map = commonInfoService.findByIDENTIFICATION_CODE(code);
        byte[] bytes = commonInfoService.getRegisterTable(map,response);
        return ResultResponse.success(bytes);
    }

//    @GetMapping("/test")
//    public ResultResponse getAll() {
//        List<CommonInfo> map = commonInfoService.selectAll();
//        return ResultResponse.success(map);
//    }

//    @Operation(summary = "查看地名的专有字段")
//    @GetMapping("/special_field")
//    public ResultResponse getSpecialField(@Parameter(description = "地名类型")String category){
//        List<Map<String,Object>> list = commonInfoService.getSpecialField(category);
//        return ResultResponse.success(list);
//    }

    //应用数据表所有地名词语分词
//    @Operation(summary = "应用数据表所有地名词语分词")
//    @GetMapping("/word")
//    public ResultResponse getWord(){
//        List<String> list = commonInfoService.getWord();
//        return ResultResponse.success(list);
//    }


//    //分类检索一级响应：返回部分字段（元数据表中配置）
//    @Operation(summary = "分类检索返回部分字段（元数据表中配置）")
//    @PostMapping("/category")
//    @Parameters({
//            @Parameter(name = "commonInfo",description = "地名应用数据库字段和值，可输入一个或多个值键对",
//                    example = "{\n" +
//                            "  \"DIAGRAM_NAME\": \"string\",\n" +
//                            "  \"TOPONYMIC_CATEGORY\": \"string\",\n" +
//                            "  \"ADMINISTRATIVE_DISTRICT\": \"string\",\n" +
//                            "  \"SPECIAL_INFORMATION\": \"{}\"\n" +
//                            "}"),
//            @Parameter(name = "DIAGRAM_NAME",description = "图上名称"),
//            @Parameter(name = "TOPONYMIC_CATEGORY",description = "地名类别"),
//            @Parameter(name = "ADMINISTRATIVE_DISTRICT",description = "所跨行政区"),
//            @Parameter(name = "SPECIAL_INFORMATION",description = "特殊信息，json格式")
//    })
//    public ResultResponse findInfo(CommonInfo commonInfo){
//        List<CommonInfo> list = commonInfoService.findInfo(commonInfo);
//        return ResultResponse.success(list);
//    }


//    //分类检索：返回全部字段
//    @Operation(summary = "分类检索返回全部字段")
//    @PostMapping ("/detail")
//    @Parameters({
//            @Parameter(name = "commonInfo",description = "地名应用数据库字段和值，可输入一个或多个值键对",
//                    example = "{\n" +
//                            "  \"DIAGRAM_NAME\": \"string\",\n" +
//                            "  \"TOPONYMIC_CATEGORY\": \"string\",\n" +
//                            "  \"ADMINISTRATIVE_DISTRICT\": \"string\",\n" +
//                            "  \"SPECIAL_INFORMATION\": \"{}\"\n" +
//                            "}"),
//            @Parameter(name = "DIAGRAM_NAME",description = "图上名称"),
//            @Parameter(name = "TOPONYMIC_CATEGORY",description = "地名类别"),
//            @Parameter(name = "ADMINISTRATIVE_DISTRICT",description = "所跨行政区"),
//            @Parameter(name = "SPECIAL_INFORMATION",description = "特殊信息，json格式")
//    })
//    public ResultResponse findByJson(CommonInfo commonInfo){
//        List<CommonInfo> list = commonInfoService.findByJson(commonInfo);
//        return ResultResponse.success(list);
//    }
//
//    @Operation(summary = "分类检索返回全部字段，分页")
//    @PostMapping ("/detail_page")
//    @Parameters({
//            @Parameter(name = "currentPage",description = "当前页",example = "1"),
//            @Parameter(name = "pageSize",description = "页面大小",example = "20"),
////            @Parameter(name = "commonInfo",description = "地名应用数据库字段和值，可输入一个或多个值键对",
////                    example = "{\n" +
////                            "  \"DIAGRAM_NAME\": \"string\",\n" +
////                            "  \"TOPONYMIC_CATEGORY\": \"string\",\n" +
////                            "  \"ADMINISTRATIVE_DISTRICT\": \"string\",\n" +
////                            "  \"SPECIAL_INFORMATION\": \"{}\"\n" +
////                            "}"),
////            @Parameter(name = "DIAGRAM_NAME",description = "图上名称"),
////            @Parameter(name = "TOPONYMIC_CATEGORY",description = "地名类别"),
////            @Parameter(name = "ADMINISTRATIVE_DISTRICT",description = "所跨行政区"),
////            @Parameter(name = "SPECIAL_INFORMATION",description = "特殊信息，json格式")
//    })
//    public ResultResponse findByJsonPage(@RequestBody CommonInfo commonInfo, int currentPage, int pageSize){
//        PageHelper.startPage(currentPage,pageSize);//后面紧跟数据库查询语句
//        List<CommonInfo> list = commonInfoService.findByJson(commonInfo);
//        PageBean<CommonInfo> pageBean = new PageBean<>(list);
//        return ResultResponse.success(pageBean);
//    }


//    //增加数据
//    @PostMapping("/add")
//    public List<CommonInfo> addCommonInfo(CommonInfo commonInfo){
//        commonInfoService.addCommonInfo(commonInfo);
//        String IDENTIFICATION_CODE = commonInfo.getIDENTIFICATION_CODE();
//        return commonInfoService.findByIDENTIFICATION_CODE(IDENTIFICATION_CODE);
//    }
//
//    //删除数据
//    @DeleteMapping("/delete/{IDENTIFICATION_CODE}")
//    public Boolean deleteMapElement(@PathVariable String IDENTIFICATION_CODE){
//        Boolean deleteMapElementSuccess = true;
//        try{
//            commonInfoService.deleteCommonInfo(IDENTIFICATION_CODE);
//        }catch (Exception e){
//            log.info("删除失败：" + e);
//            deleteMapElementSuccess = false;
//        }
//        return deleteMapElementSuccess;
//    }
//
//    //更新数据
//    @PutMapping("/update")
//    public List<CommonInfo> updateCommonInfo(CommonInfo commonInfo){
//        commonInfoService.updateCommonInfo(commonInfo);
//        String IDENTIFICATION_CODE = commonInfo.getIDENTIFICATION_CODE();
//        return commonInfoService.findByIDENTIFICATION_CODE(IDENTIFICATION_CODE);
//    }



}
