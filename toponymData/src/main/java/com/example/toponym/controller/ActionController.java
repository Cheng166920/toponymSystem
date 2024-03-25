package com.example.toponym.controller;

import com.example.toponym.common.BizException;
import com.example.toponym.model.Action;
import com.example.toponym.model.PageBean;
import com.example.toponym.model.ResultResponse;
import com.example.toponym.model.Toponym;
import com.example.toponym.service.ActionService;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
//import net.sf.json.JSONObject;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "地名管理模块",description = "地名的增删改操作")
@RequestMapping("/toponym/action")
public class  ActionController {
    @Autowired
    private ActionService actionService;

    @Operation(summary = "公众申报：将新增数据写入生产数据库")
    @Parameters({
            @Parameter(name = "STANDARD_NAME",description = "地点名称"),
            @Parameter(name = "TOPONYMIC_CATEGORY",description = "地名类别，可选类别参考接口http://localhost:8080/toponym/info/all_category提供的该类地名的最详细类别。例如地名类别为湖泊时，湖泊无下一级划分，因此湖泊为最详细类别。",example = "湖泊"),
            @Parameter(name = "GEOM",description = "地点位置，GeoJSON格式",example = "{\n" +
                    "  \"type\": \"Point\",\n" +
                    "  \"coordinates\": [\n" +
                    "    112.178433333333615,\n" +
                    "    30.35997222222213\n" +
                    "  ]\n" +
                    "}"),
            @Parameter(name = "ESTABLISHMENT_YEAR",description = "设立年份，可输入具体的年份或某一时期，字段长度限制为20，如2000年、民国时期",example = "2000年"),
            @Parameter(name = "TOPONYM_ORIGIN",description = "地名来历"),
            @Parameter(name = "TOPONYM_MEANING",description = "地点含义"),
            @Parameter(name = "TOPONYM_HISTORY",description = "历史沿革"),
            @Parameter(name = "ADMINISTRATIVE_DISTRICT",description = "所跨行政区，精确到乡镇级别，目前只有湖北省的数据，可参考接口http://localhost:8080/doorplate/check/city_tree查看行政区划，例如湖北省荆州市荆州区李埠镇",example = "湖北省荆州市荆州区李埠镇"),
            @Parameter(name = "REMARK",description = "备注"),
            @Parameter(name = "USER",description = "申请用户"),
            @Parameter(name = "CONTACT_NUMBER",description = "联系电话"),
            @Parameter(name = "ORGANIZATION",description = "组织")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "新增地名数据结构",required = true)
    @PostMapping(value = "/add_toponym",produces = "application/json;charset=UTF-8")
    public ResultResponse addToponymAction(@RequestBody Toponym toponym, HttpServletRequest request){
        Action action_list = toponymToAction(toponym,"公众申报");
        if(!isJsonValid(action_list.getACTION(), action_list.getCONTENT())){
            throw new BizException("4007","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        JSONObject obj = JSONObject.parseObject(String.valueOf(action_list.getCONTENT()));
        obj.put("IDENTIFICATION_CODE",actionService.setIdentificationCode());
        action_list.setCONTENT(obj);
        Boolean result = actionService.isValid(action_list);//判断数据有效性
        while(!result) {
            obj.remove("IDENTIFICATION_CODE");
            obj.put("IDENTIFICATION_CODE",actionService.setIdentificationCode());
            action_list.setCONTENT(obj);
            result = actionService.isValid(action_list);//判断数据有效性
        }
        Map<String, Object> newAction = actionService.addAction(action_list);
        return ResultResponse.success(newAction);
    }

    @Operation(summary = "公众纠错：将修改数据写入生产数据库")
    @Parameters({
            @Parameter(name = "IDENTIFICATION_CODE",description = "空间数据标识码",example = "a5270792-2ff3-4809-b90c-abf93986411e"),
            @Parameter(name = "STANDARD_NAME",description = "地点名称"),
            @Parameter(name = "TOPONYMIC_CATEGORY",description = "地名类别，可选类别参考接口http://localhost:8080/toponym/info/all_category提供的该类地名的最详细类别。例如地名类别为湖泊时，湖泊无下一级划分，因此湖泊为最详细类别。",example = "湖泊"),
            @Parameter(name = "GEOM",description = "地点位置，GeoJSON格式",example = "{\n" +
                    "  \"type\": \"Point\",\n" +
                    "  \"coordinates\": [\n" +
                    "    112.178433333333615,\n" +
                    "    30.35997222222213\n" +
                    "  ]\n" +
                    "}"),
            @Parameter(name = "ESTABLISHMENT_YEAR",description = "设立年份，可输入具体的年份或某一时期，字段长度限制为20，如2000年、民国时期",example = "2000年"),
            @Parameter(name = "TOPONYM_ORIGIN",description = "地名来历"),
            @Parameter(name = "TOPONYM_MEANING",description = "地点含义"),
            @Parameter(name = "TOPONYM_HISTORY",description = "历史沿革"),
            @Parameter(name = "ADMINISTRATIVE_DISTRICT",description = "所跨行政区，精确到乡镇级别，目前只有湖北省的数据，可参考接口http://localhost:8080/doorplate/check/city_tree查看行政区划，例如湖北省荆州市荆州区李埠镇",example = "湖北省荆州市荆州区李埠镇"),
            @Parameter(name = "REMARK",description = "备注"),
            @Parameter(name = "USER",description = "申请用户"),
            @Parameter(name = "CONTACT_NUMBER",description = "联系电话"),
            @Parameter(name = "ORGANIZATION",description = "组织")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "地名修改数据结构，当属性无需修改时，不需要输入该字段及其对应的值。",required = true)
    @PostMapping(value = "/update_toponym",produces = "application/json;charset=UTF-8")
    public ResultResponse updateToponymAction(@RequestBody Toponym toponym){
        Action action_list = toponymToAction(toponym,"公众纠错");
        if(!isJsonValid(action_list.getACTION(), action_list.getCONTENT())){
            throw new BizException("4007","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        //判断数据有效性
        Boolean result = actionService.isValid(action_list);
        if(result) {
            Map<String, Object> newAction = actionService.addAction(action_list);
            return ResultResponse.success(newAction);
        }
        else
            return ResultResponse.success("数据无效");
    }

    @Operation(summary = "业务删除：将删除数据写入生产数据库")
    @Parameters({
            @Parameter(name = "IDENTIFICATION_CODE",description = "空间数据标识码",example = "a5270792-2ff3-4809-b90c-abf93986411e"),
    })
    @GetMapping(value = "/delete_toponym",produces = "application/json;charset=UTF-8")
    public ResultResponse deleteToponymAction(String IDENTIFICATION_CODE, HttpServletRequest request){
        Toponym toponym = new Toponym();
        toponym.setIDENTIFICATION_CODE(IDENTIFICATION_CODE);
        Action action_list = toponymToAction(toponym,"业务删除");
        if(!isJsonValid(action_list.getACTION(), action_list.getCONTENT())){
            throw new BizException("4007","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        Boolean result = actionService.isValid(action_list);//判断数据有效性
        if(result) {
            Map<String, Object> newAction = actionService.addAction(action_list);
            return ResultResponse.success(newAction);
        }
        else
            return ResultResponse.success("数据无效");
    }

    @Operation(summary = "地名审批")
    @Parameters({
            @Parameter(name = "gid",description = "地名生产数据表自增序号",example = "1",required = true),
            @Parameter(name = "review",description = "审核状态，阈值：审核通过、审核未通过。",example = "审核通过",required = true)
    })
    @GetMapping("/review")
    public ResultResponse updateReview(Integer gid, String review){
        Action action_list = new Action();
        String[] str = {"未审核","审核通过","审核未通过"};
        List<String> review_list = Arrays.asList(str);
        if(!review_list.contains(review)){
            throw new BizException("4010","客户端请求的参数错误，请检查提交参数的正确性。");
        }
        action_list.setREVIEW(review);
        action_list.setGID(gid);
        Map<String, Object> action = actionService.updateReview(action_list);
        return ResultResponse.success(action);
    }

//    @Operation(summary = "更新生产表数据")
//    @Parameters({
//            @Parameter(name = "GID",description = "地名生产数据表自增序号",example = "1",required = true),
//            @Parameter(name = "ACTION",description = "需要修改的操作，其阈值为：业务新增、业务删除、业务修改、公众申报、公众纠错",example = "业务新增"),
//            @Parameter(name = "CONTENT",description = "需要修改的数据内容,json格式。字段无要求。",example = "{}")
//    })
//    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "地名生产表数据结构，请输入需要更新的数据的序号，以及该数据更新后的内容或操作。当内容或操作无需修改时，不需要输入该字段及其对应的值",required = true)
//    @PostMapping("/update")
//    public ResultResponse updateAction(@RequestBody Action action_list){
//        actionService.updateAction(action_list);
//        return ResultResponse.success();
//    }

    @Operation(summary = "删除生产表数据")
    @GetMapping("/delete")
    public ResultResponse deleteMapElement(@Parameter(description = "数据库自增序号") Integer gid){
        actionService.deleteAction(gid);
        return ResultResponse.success(true);
    }

    @Operation(summary = "查看生产表所有数据，分页")
    @Parameters({
            @Parameter(name = "currentPage",description = "当前页",example = "1"),
            @Parameter(name = "pageSize",description = "页面大小",example = "20")
    })
    @GetMapping("/list_page")
    public ResultResponse findAllPage(int currentPage, int pageSize){
        PageHelper.startPage(currentPage,pageSize);//后面紧跟数据库查询语句
        List<Map<String,Object>> list = actionService.findAll();
        PageBean<Map<String,Object>> pageBean = new PageBean<>(list);
        return ResultResponse.success(pageBean);
    }

    @Operation(summary = "查看生产表单条数据")
    @GetMapping("/gid")
    public ResultResponse findByGID(@Parameter(description = "序号")Integer gid){
        Map<String,Object> map = actionService.findByGID(gid);
        return ResultResponse.success(map);
    }

    @Operation(summary = "业务新增：管理员新增地名数据")
    @Parameters({
            @Parameter(name = "STANDARD_NAME",description = "地点名称"),
            @Parameter(name = "TOPONYMIC_CATEGORY",description = "地名类别，可选类别参考接口http://localhost:8080/toponym/info/all_category提供的该类地名的最详细类别。例如地名类别为湖泊时，湖泊无下一级划分，因此湖泊为最详细类别。",example = "湖泊"),
            @Parameter(name = "GEOM",description = "地点位置，GeoJSON格式",example = "{\n" +
                    "  \"type\": \"Point\",\n" +
                    "  \"coordinates\": [\n" +
                    "    112.178433333333615,\n" +
                    "    30.35997222222213\n" +
                    "  ]\n" +
                    "}"),
            @Parameter(name = "ESTABLISHMENT_YEAR",description = "设立年份，可输入具体的年份或某一时期，字段长度限制为20，如2000年、民国时期",example = "2000年"),
            @Parameter(name = "TOPONYM_ORIGIN",description = "地名来历"),
            @Parameter(name = "TOPONYM_MEANING",description = "地点含义"),
            @Parameter(name = "TOPONYM_HISTORY",description = "历史沿革"),
            @Parameter(name = "ADMINISTRATIVE_DISTRICT",description = "所跨行政区，精确到乡镇级别，目前只有湖北省的数据，可参考接口http://localhost:8080/doorplate/check/city_tree查看行政区划，例如湖北省荆州市荆州区李埠镇",example = "湖北省荆州市荆州区李埠镇"),
            @Parameter(name = "REMARK",description = "备注"),
            @Parameter(name = "USER",description = "申请用户"),
            @Parameter(name = "CONTACT_NUMBER",description = "联系电话"),
            @Parameter(name = "ORGANIZATION",description = "组织")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "地名新增数据结构",required = true)
    @PostMapping(value = "/admin_add",produces = "application/json;charset=UTF-8")
    public ResultResponse adminAddAction(@RequestBody Toponym toponym, HttpServletRequest request){
        Action action_list = toponymToAction(toponym,"业务新增");
        if(!isJsonValid(action_list.getACTION(), action_list.getCONTENT())){
            throw new BizException("4007","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        JSONObject obj = JSONObject.parseObject(String.valueOf(action_list.getCONTENT()));
        obj.put("IDENTIFICATION_CODE",actionService.setIdentificationCode());
        action_list.setCONTENT(obj);
        Boolean result = actionService.isValid(action_list);//判断数据有效性
        while(!result) {
            obj.remove("IDENTIFICATION_CODE");
            obj.put("IDENTIFICATION_CODE",actionService.setIdentificationCode());
            action_list.setCONTENT(obj);
            result = actionService.isValid(action_list);//判断数据有效性
        }
        Map<String, Object> newAction = actionService.addAction(action_list);
        action_list.setREVIEW("审核通过");
        actionService.updateReview(action_list);
        Map<String,Object> map = actionService.findByGID(action_list.getGID());
        return ResultResponse.success(map);
    }

    @Operation(summary = "业务修改：管理员修改地名数据")
    @Parameters({
            @Parameter(name = "IDENTIFICATION_CODE",description = "空间数据标识码"),
            @Parameter(name = "STANDARD_NAME",description = "地点名称"),
            @Parameter(name = "TOPONYMIC_CATEGORY",description = "地名类别，可选类别参考接口http://localhost:8080/toponym/info/all_category提供的该类地名的最详细类别。例如地名类别为湖泊时，湖泊无下一级划分，因此湖泊为最详细类别。",example = "湖泊"),
            @Parameter(name = "GEOM",description = "地点位置，GeoJSON格式",example = "{\n" +
                    "  \"type\": \"Point\",\n" +
                    "  \"coordinates\": [\n" +
                    "    112.178433333333615,\n" +
                    "    30.35997222222213\n" +
                    "  ]\n" +
                    "}"),
            @Parameter(name = "ESTABLISHMENT_YEAR",description = "设立年份，可输入具体的年份或某一时期，字段长度限制为20，如2000年、民国时期",example = "2000年"),
            @Parameter(name = "TOPONYM_ORIGIN",description = "地名来历"),
            @Parameter(name = "TOPONYM_MEANING",description = "地点含义"),
            @Parameter(name = "TOPONYM_HISTORY",description = "历史沿革"),
            @Parameter(name = "ADMINISTRATIVE_DISTRICT",description = "所跨行政区，精确到乡镇级别，目前只有湖北省的数据，可参考接口http://localhost:8080/doorplate/check/city_tree查看行政区划，例如湖北省荆州市荆州区李埠镇",example = "湖北省荆州市荆州区李埠镇"),
            @Parameter(name = "REMARK",description = "备注"),
            @Parameter(name = "USER",description = "申请用户"),
            @Parameter(name = "CONTACT_NUMBER",description = "联系电话"),
            @Parameter(name = "ORGANIZATION",description = "组织")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "地名修改数据结构，当属性无需修改时，不需要输入该字段及其对应的值。",required = true)
    @PostMapping(value = "/admin_update",produces = "application/json;charset=UTF-8")
    public ResultResponse adminUpdateToponymAction(@RequestBody Toponym toponym, HttpServletRequest request){
        Action action_list = toponymToAction(toponym,"业务修改");
        if(!isJsonValid(action_list.getACTION(), action_list.getCONTENT())){
            throw new BizException("4007","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        Boolean result = actionService.isValid(action_list);//判断数据有效性
        if(result) {
            Map<String, Object> newAction = actionService.addAction(action_list);
            action_list.setREVIEW("审核通过");
            actionService.updateReview(action_list);
            Map<String,Object> map = actionService.findByGID(action_list.getGID());
            return ResultResponse.success(map);
        }
        else
            return ResultResponse.success("数据无效");


    }

    @Operation(summary = "业务删除：管理员删除地名数据")
    @Parameters({
            @Parameter(name = "IDENTIFICATION_CODE",description = "空间数据标识码"),
    })
    @GetMapping(value = "/admin_delete",produces = "application/json;charset=UTF-8")
    public ResultResponse adminDeleteToponymAction(String IDENTIFICATION_CODE, HttpServletRequest request){
        Toponym toponym = new Toponym();
        toponym.setIDENTIFICATION_CODE(IDENTIFICATION_CODE);
        Action action_list = toponymToAction(toponym,"业务删除");
        if(!isJsonValid(action_list.getACTION(), action_list.getCONTENT())){
            throw new BizException("4007","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        Boolean result = actionService.isValid(action_list);//判断数据有效性
        if(result) {
            Map<String, Object> newAction = actionService.addAction(action_list);
            action_list.setREVIEW("审核通过");
            actionService.updateReview(action_list);
            Map<String,Object> map = actionService.findByGID(action_list.getGID());
            return ResultResponse.success(map);
        }
        else
            return ResultResponse.success("数据无效");
    }


//    @Operation(summary = "管理员更新生产表数据")
//    @Parameters({
//            @Parameter(name = "GID",description = "地名生产数据表自增序号",example = "1",required = true),
//            @Parameter(name = "ACTION",description = "需要修改的操作，其阈值为：业务新增、业务删除、业务修改",example = "业务新增"),
//            @Parameter(name = "CONTENT",description = "需要修改的数据内容,json格式。字段无要求。",example = "{}")
//    })
//    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "地名生产表数据结构，请输入需要更新的数据的序号，以及该数据更新后的内容或操作。当内容或操作无需修改时，不需要输入该字段及其对应的值",required = true)
//    @PostMapping("/adminUpdate")
//    public ResultResponse adminUpdateAction(@RequestBody Action action_list){
//        actionService.updateAction(action_list);
//        action_list.setREVIEW("审核通过");
//        actionService.updateReview(action_list);
//        return ResultResponse.success();
//    }

    //判断提交的json字符串的规范性
    private Boolean isJsonValid(String action, Object Object){
        switch (action) {
            case "业务新增":
            case "公众申报":
                List<String> keyList = Arrays.asList("STANDARD_NAME","TOPONYMIC_CATEGORY","GEOM","ESTABLISHMENT_YEAR","TOPONYM_ORIGIN","TOPONYM_MEANING","TOPONYM_HISTORY","ADMINISTRATIVE_DISTRICT");
                for (String key : keyList) {
                    if (JSONObject.parseObject(String.valueOf(Object)).containsKey(key))
                        continue;
                    return false;
                }
                return true;
            case "业务修改":
            case "业务删除":
            case "公众纠错":
                return JSONObject.parseObject(String.valueOf(Object)).containsKey("IDENTIFICATION_CODE");
        }
        return false;
    }

    private Action toponymToAction(Object toponym, String action){
        Action result = new Action();
        result.setACTION(action);
        result.setCONTENT(new Gson().toJson(toponym));
        return result;
    }
}
