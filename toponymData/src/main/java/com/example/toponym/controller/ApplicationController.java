package com.example.toponym.controller;

import cn.hutool.http.HttpRequest;
import com.example.toponym.common.BizException;
import com.example.toponym.model.*;
import com.example.toponym.service.ActionService;
import com.example.toponym.service.BoundaryService;
import com.example.toponym.service.CommonInfoService;
import com.example.toponym.service.DoorPlateService;
import com.example.toponym.service.impl.ExceptionEnum;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Tag(name = "应用拓展模块",description = "审核信息")
@Slf4j
@RestController
@RequestMapping("/application")
@CrossOrigin
public class ApplicationController {
    @Autowired
    private ActionService actionService;
    @Autowired
    private DoorPlateService doorPlateService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private CommonInfoService commonInfoService;

    @Operation(summary = "获取审批请求的详细信息")
    @Parameters({
            @Parameter(name = "type",description = "审批类型，其阈值为：toponym_1、toponym_2、toponym_3、boundary_1、doorplate_1、doorplate_2、doorplate_3、doorplate_4。",example = "toponym_1",required = true),
            @Parameter(name = "id",description = "生产数据表序号",example = "1",required = true)
    })
    @GetMapping("/detail")
    public ResultResponse findByID(String type, Integer id){
        switch (type) {
            case "toponym_1":
            case "toponym_3":
                Map<String,Object> toponym = actionService.findByGID(id);
                return ResultResponse.success(toponym);
            case "toponym_2":
                Map<String,Object> update_toponym = actionService.findUpdateByGID(id);
                return ResultResponse.success(update_toponym);
            case "boundary_1":
                Map<String,Object> boundary = boundaryService.findByGID(id);
                return ResultResponse.success(boundary);
            case "doorplate_2":
            case "doorplate_4":
                Map<String,Object> update_doorplate = doorPlateService.findUpdateByGID(id);
                return ResultResponse.success(update_doorplate);
            case "doorplate_1":
            case "doorplate_3":
                Action doorplate = doorPlateService.findByGID(id);
                return ResultResponse.success(doorplate);
        }
        return ResultResponse.error(ExceptionEnum.PARAM_NOT_MATCH);

    }

    @Operation(summary = "审批整合")
    @Parameters({
            @Parameter(name = "type",description = "审批类型，其阈值为：toponym_1、toponym_2、toponym_3、boundary_1、doorplate_1、doorplate_2、doorplate_3、doorplate_4。",example = "toponym_1",required = true),
            @Parameter(name = "gid",description = "生产数据表自增序号",example = "1",required = true),
            @Parameter(name = "review",description = "审核状态，其阈值为：审核通过、审核未通过。",example = "审核通过",required = true)
    })
    @GetMapping("/review")
    public ResultResponse updateReview(String type, Integer gid, String review){
        String[] str = {"未审核","审核通过","审核未通过"};
        List<String> review_list = Arrays.asList(str);
        if(!review_list.contains(review)){
            throw new BizException("4010","客户端请求的参数错误，请检查提交参数的正确性。");
        }
        switch (type) {
            case "toponym_1":
            case "toponym_2":
            case "toponym_3":
                Action action_list = new Action();
                action_list.setREVIEW(review);
                action_list.setGID(gid);
                Map<String, Object> action = actionService.updateReview(action_list);
                return ResultResponse.success(action);
            case "doorplate_1":
            case "doorplate_2":
            case "doorplate_3":
            case "doorplate_4":
                doorPlateService.updateReview(review,gid);
                return ResultResponse.success(doorPlateService.findByGID(gid));
            case "boundary_1":
                Map<String,Object> list = boundaryService.downloadReview(gid,review);
                return ResultResponse.success(list);
        }
        return ResultResponse.error(ExceptionEnum.PARAM_NOT_MATCH);

    }

    @Operation(summary = "地名公众申报请求")
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
        JSONObject obj = JSONObject.fromObject(action_list.getCONTENT());
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
            //发送消息
        String token = request.getHeader("Token");
        log.info(token);
        com.alibaba.fastjson.JSONObject message = new com.alibaba.fastjson.JSONObject();
        message.put("applicationType","toponym_1");
        message.put("applicationName","地名公众申报");
        message.put("mobile",obj.getString("CONTACT_NUMBER"));
        message.put("applicationId",newAction.get("GID"));
        String res = HttpRequest.post("http://zsdl.myqnapcloud.com:8089/api/process/pushProcess").
                header("Token", token).
                body(com.alibaba.fastjson.JSONObject.toJSONString(message)).execute().body();
        log.info(res);
        return ResultResponse.success(newAction);
    }

    @Operation(summary = "地名公众纠错请求")
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
    public ResultResponse updateToponymAction(@RequestBody Toponym toponym, HttpServletRequest request){
        Action action_list = toponymToAction(toponym,"公众纠错");
        if(!isJsonValid(action_list.getACTION(), action_list.getCONTENT())){
            throw new BizException("4007","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        //判断数据有效性
        Boolean result = actionService.isValid(action_list);
        JSONObject obj = JSONObject.fromObject(action_list.getCONTENT());
        if(result) {
            Map<String, Object> newAction = actionService.addAction(action_list);
            //发送消息
            String token = request.getHeader("Token");
            log.info(token);
            com.alibaba.fastjson.JSONObject message = new com.alibaba.fastjson.JSONObject();
            message.put("applicationType","toponym_2");
            message.put("applicationName","地名公众纠错");
            message.put("mobile",obj.getString("CONTACT_NUMBER"));
            message.put("applicationId",newAction.get("GID"));
            String res = HttpRequest.post("http://zsdl.myqnapcloud.com:8089/api/process/pushProcess").
                    header("Token", token).
                    body(com.alibaba.fastjson.JSONObject.toJSONString(message)).execute().body();
            log.info(res);
            return ResultResponse.success(newAction);
        }
        else
            return ResultResponse.success("数据无效");
    }

    @Operation(summary = "地名业务删除请求")
    @Parameters({
            @Parameter(name = "IDENTIFICATION_CODE",description = "空间数据标识码",example = "a5270792-2ff3-4809-b90c-abf93986411e"),
            @Parameter(name = "CONTACT_NUMBER",description = "手机号码",example = "13222256985")
    })
    @GetMapping(value = "/delete_toponym",produces = "application/json;charset=UTF-8")
    public ResultResponse deleteToponymAction(String IDENTIFICATION_CODE, String CONTACT_NUMBER, HttpServletRequest request){
        Toponym toponym = new Toponym();
        toponym.setIDENTIFICATION_CODE(IDENTIFICATION_CODE);
        toponym.setCONTACT_NUMBER(CONTACT_NUMBER);
        Action action_list = toponymToAction(toponym,"业务删除");
        if(!isJsonValid(action_list.getACTION(), action_list.getCONTENT())){
            throw new BizException("4007","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        Boolean result = actionService.isValid(action_list);//判断数据有效性
        JSONObject obj = JSONObject.fromObject(action_list.getCONTENT());
        if(result) {
            Map<String, Object> newAction = actionService.addAction(action_list);
            //发送消息
            String token = request.getHeader("Token");
            log.info(token);
            com.alibaba.fastjson.JSONObject message = new com.alibaba.fastjson.JSONObject();
            message.put("applicationType","toponym_3");
            message.put("applicationName","地名业务删除");
            message.put("mobile",CONTACT_NUMBER);
            message.put("applicationId",newAction.get("GID"));
            String res = HttpRequest.post("http://zsdl.myqnapcloud.com:8089/api/process/pushProcess").
                    header("Token", token).
                    body(com.alibaba.fastjson.JSONObject.toJSONString(message)).execute().body();
            log.info(res);
            return ResultResponse.success(newAction);
        }
        else
            return ResultResponse.success("数据无效");
    }

    @Operation(summary = "界线申领请求")
    @GetMapping("/boundary_download")
    public ResultResponse downloadRequest(@Parameter(description = "标识码",example = "66b954e1-689d-40f5-a100-1eed8113ca60")String code,@Parameter(description = "电话号码")String user,
                                          HttpServletRequest request){
        Map<String,Object> map = boundaryService.downloadRequest(code,user);
        String token = request.getHeader("Token");
        log.info(token);
        com.alibaba.fastjson.JSONObject message = new com.alibaba.fastjson.JSONObject();
        message.put("applicationType","boundary_1");
        message.put("applicationName","界线申领");
        message.put("mobile",user);
        message.put("applicationId",map.get("GID"));
        String res = HttpRequest.post("http://zsdl.myqnapcloud.com:8089/api/process/pushProcess").
                header("Token", token).
                body(com.alibaba.fastjson.JSONObject.toJSONString(message)).execute().body();
        log.info(res);
        return ResultResponse.success(map);
    }


    @Operation(summary = "门牌业务新增请求")
    @Parameters({
            @Parameter(name = "province",description = "省份，必填",example = "湖北省"),
            @Parameter(name = "city",description = "市级，必填",example = "荆州市"),
            @Parameter(name = "district",description = "区县级，必填",example = "荆州区"),
            @Parameter(name = "township",description = "乡镇级，必填",example = "郢城镇"),
            @Parameter(name = "village",description = "村/社区，必填",example = "新生村"),
            @Parameter(name = "road",description = "道路",example = "九阳大道"),
            @Parameter(name = "road_direction",description = "道路方位走向",example = "南北走向"),
            @Parameter(name = "estate",description = "小区",example = "五台小区"),
            @Parameter(name = "building",description = "楼栋",example = "1栋"),
            @Parameter(name = "unit",description = "单元",example = "1单元"),
            @Parameter(name = "doorplate",description = "门牌号码",example = "1号"),
            @Parameter(name = "doorplate_sub",description = "门牌号码附号",example = "附1号"),
            @Parameter(name = "room",description = "房间号",example = "101室"),
            @Parameter(name = "house_license",description = "门牌证号",example = "421003103000999"),
            @Parameter(name = "license_time",description = "制证时间",example = "2023-05-20 00:00:00"),
            @Parameter(name = "category",description = "门牌类别，可选【商户、住宅、机关企事业单位、企业、商住、单位、其他】",example = "商户"),
            @Parameter(name = "owner",description = "产权人",example = "张三"),
            @Parameter(name = "owner_phone",description = "产权人联系电话",example = "8888888"),
            @Parameter(name = "construction",description = "建筑物名称",example = "测试建筑"),
            @Parameter(name = "principal",description = "负责人",example = "李四"),
            @Parameter(name = "principal_phone",description = "负责人联系电话",example = "8888888"),
            @Parameter(name = "collect_time",description = "采集时间",example = "2023-05-20 00:00:00"),
            @Parameter(name = "linear_toponym",description = "关联线状地名",example = "九阳大道"),
            @Parameter(name = "point_toponym",description = "关联点状地名",example = "测试建筑"),
            @Parameter(name = "remarks",description = "备注",example = "测试数据"),
            @Parameter(name = "photo_name",description = "照片文件名",example = "照片_30.362629-112.203631-0.0_20230815175610001.jpg"),
            @Parameter(name = "geom",description = "地理坐标Point,swagger中提交需要引号和转译符",example = "{\"type\":\"Point\",\"coordinates\":[112.160426843,30.337650579]}"),
            @Parameter(name = "contact_number",description = "公众提交的电话",example = "13677778888")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "门牌号表数据结构，请输入需要添加的门牌信息，uid不需要提交。五级行政区划（province、city、district、township、village）必填，用于生成uid",required = true)
    @PostMapping("/add_doorplate")
    public ResultResponse addCheckDoorplate(@RequestBody DoorPlateOther doorPlateElement,HttpServletRequest request){
        JSONObject doorPlateElement_toJSON = JSONObject.fromObject(doorPlateElement);
        if(!isJsonValid(doorPlateElement_toJSON)){
            throw new BizException("4009","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        Action action = doorplateToAction(doorPlateElement,"业务新增");
        int actionState = doorPlateService.addAction(action);
        if(actionState != 0){
            throw new BizException("4007","客户端请求的参数错误，数据库已有该门牌证号");
        }
        //发送消息
        String token = request.getHeader("Token");
        log.info(token);
        com.alibaba.fastjson.JSONObject message = new com.alibaba.fastjson.JSONObject();
        message.put("applicationType","doorplate_1");
        message.put("applicationName","门牌业务新增");
        message.put("mobile",doorPlateElement.getContact_number());
        message.put("applicationId",action.getGID());
        String res = HttpRequest.post("http://zsdl.myqnapcloud.com:8089/api/process/pushProcess").
                header("Token", token).
                body(com.alibaba.fastjson.JSONObject.toJSONString(message)).execute().body();
        log.info(res);
        return ResultResponse.success(doorPlateService.findByGID(action.getGID()));
    }


    @Operation(summary = "门牌业务修改请求")
    @Parameters({
            @Parameter(name = "uid",description = "地址的唯一标识码，必填",example = "例如：4210031060011400001"),
            @Parameter(name = "road",description = "道路",example = "九阳大道"),
            @Parameter(name = "road_direction",description = "道路方位走向",example = "南北走向"),
            @Parameter(name = "estate",description = "小区",example = "五台小区"),
            @Parameter(name = "building",description = "楼栋",example = "1栋"),
            @Parameter(name = "unit",description = "单元",example = "1单元"),
            @Parameter(name = "doorplate",description = "门牌号码",example = "1号"),
            @Parameter(name = "doorplate_sub",description = "门牌号码附号",example = "附1号"),
            @Parameter(name = "room",description = "房间号",example = "101室"),
            @Parameter(name = "house_license",description = "门牌证号",example = "421003103000999"),
            @Parameter(name = "license_time",description = "制证时间",example = "2023-05-20 00:00:00"),
            @Parameter(name = "category",description = "门牌类别，可选【商户、住宅、机关企事业单位、企业、商住、单位、其他】",example = "商户"),
            @Parameter(name = "owner",description = "产权人",example = "张三"),
            @Parameter(name = "owner_phone",description = "产权人联系电话",example = "8888888"),
            @Parameter(name = "construction",description = "建筑物名称",example = "测试建筑"),
            @Parameter(name = "principal",description = "负责人",example = "李四"),
            @Parameter(name = "principal_phone",description = "负责人联系电话",example = "8888888"),
            @Parameter(name = "collect_time",description = "采集时间",example = "2023-05-20 00:00:00"),
            @Parameter(name = "linear_toponym",description = "关联线状地名",example = "九阳大道"),
            @Parameter(name = "point_toponym",description = "关联点状地名",example = "测试建筑"),
            @Parameter(name = "remarks",description = "备注",example = "测试数据"),
            @Parameter(name = "photo_name",description = "照片文件名",example = "照片_30.362629-112.203631-0.0_20230815175610001.jpg"),
            @Parameter(name = "geom",description = "地理坐标Point",example = "{\"type\":\"Point\",\"coordinates\":[112.160426843,30.337650579]}"),
            @Parameter(name = "contact_number",description = "公众提交的电话",example = "13677778888")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "门牌号表数据结构，请输入需要修改的门牌信息，uid必填，五级行政区划（province、city、district、township、village）默认不做修改",required = true)
    @PostMapping("/update_doorplate")
    public ResultResponse updateCheckDoorplate(@RequestBody DoorPlateOther doorPlateElement,HttpServletRequest request){
        JSONObject doorPlateElement_toJSON = JSONObject.fromObject(doorPlateElement);
        if(!isDoorplateJsonValid("业务修改",doorPlateElement_toJSON)){
            throw new BizException("4009","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        Action action = doorplateToAction(doorPlateElement,"业务修改");
        int actionState = doorPlateService.addAction(action);
        if(actionState != 0){
            throw new BizException("4008","客户端请求的参数错误，检查标识码正确性。");
        }
        //发送消息
        String token = request.getHeader("Token");
        log.info(token);
        com.alibaba.fastjson.JSONObject message = new com.alibaba.fastjson.JSONObject();
        message.put("applicationType","doorplate_2");
        message.put("applicationName","门牌业务修改");
        message.put("mobile",doorPlateElement.getContact_number());
        message.put("applicationId",action.getGID());
        String res = HttpRequest.post("http://zsdl.myqnapcloud.com:8089/api/process/pushProcess").
                header("Token", token).
                body(com.alibaba.fastjson.JSONObject.toJSONString(message)).execute().body();
        log.info(res);
        return ResultResponse.success(doorPlateService.findByGID(action.getGID()));

    }

    @Operation(summary = "门牌业务删除请求")
    @Parameters({
            @Parameter(name = "uid",description = "需要删除地址的唯一标识码（必须）")
    })
    @GetMapping("/delete_doorplate")
    public ResultResponse deleteCheckDoorplate(@Parameter(description = "门牌号标识码")String uid,@Parameter(description = "公众提交的电话")String contact_number,HttpServletRequest request){
        JSONObject json = new JSONObject();
        json.put("uid",uid);
        json.put("contact_number",contact_number);
        Action action = doorplateToAction(json,"业务删除");
        int actionState = doorPlateService.addAction(action);
        if(actionState != 0){
            throw new BizException("4008","客户端请求的参数错误，检查标识码正确性。");
        }
        //发送消息
        String token = request.getHeader("Token");
        log.info(token);
        com.alibaba.fastjson.JSONObject message = new com.alibaba.fastjson.JSONObject();
        message.put("applicationType","doorplate_3");
        message.put("applicationName","门牌业务删除");
        message.put("mobile",contact_number);
        message.put("applicationId",action.getGID());
        String res = HttpRequest.post("http://zsdl.myqnapcloud.com:8089/api/process/pushProcess").
                header("Token", token).
                body(com.alibaba.fastjson.JSONObject.toJSONString(message)).execute().body();
        log.info(res);
        return ResultResponse.success(doorPlateService.findByGID(action.getGID()));
    }


    @Operation(summary = "门牌公众纠错请求")
    @Parameters({
            @Parameter(name = "uid",description = "地址的唯一标识码，必填",example = "例如：4210031060011400001"),
            @Parameter(name = "road",description = "道路",example = "九阳大道"),
            @Parameter(name = "road_direction",description = "道路方位走向",example = "南北走向"),
            @Parameter(name = "estate",description = "小区",example = "五台小区"),
            @Parameter(name = "building",description = "楼栋",example = "1栋"),
            @Parameter(name = "unit",description = "单元",example = "1单元"),
            @Parameter(name = "doorplate",description = "门牌号码",example = "1号"),
            @Parameter(name = "doorplate_sub",description = "门牌号码附号",example = "附1号"),
            @Parameter(name = "room",description = "房间号",example = "101室"),
            @Parameter(name = "house_license",description = "门牌证号",example = "421003103000999"),
            @Parameter(name = "license_time",description = "制证时间",example = "2023-05-20 00:00:00"),
            @Parameter(name = "category",description = "门牌类别，可选【商户、住宅、机关企事业单位、企业、商住、单位、其他】",example = "商户"),
            @Parameter(name = "owner",description = "产权人",example = "张三"),
            @Parameter(name = "owner_phone",description = "产权人联系电话",example = "8888888"),
            @Parameter(name = "construction",description = "建筑物名称",example = "测试建筑"),
            @Parameter(name = "principal",description = "负责人",example = "李四"),
            @Parameter(name = "principal_phone",description = "负责人联系电话",example = "8888888"),
            @Parameter(name = "collect_time",description = "采集时间",example = "2023-05-20 00:00:00"),
            @Parameter(name = "linear_toponym",description = "关联线状地名",example = "九阳大道"),
            @Parameter(name = "point_toponym",description = "关联点状地名",example = "测试建筑"),
            @Parameter(name = "remarks",description = "备注",example = "测试数据"),
            @Parameter(name = "photo_name",description = "照片文件名",example = "照片_30.362629-112.203631-0.0_20230815175610001.jpg"),
            @Parameter(name = "geom",description = "地理坐标Point",example = "{\"type\":\"Point\",\"coordinates\":[112.160426843,30.337650579]}"),
            @Parameter(name = "contact_number",description = "公众提交的电话",example = "13677778888")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "门牌号表数据结构，请输入需要写入的数据内容。uid必填，五级行政区划（province、city、district、township、village）默认不做修改",required = true)
    @PostMapping("/action_doorplate")
    public ResultResponse addAction(@RequestBody DoorPlateOther doorPlateOther,HttpServletRequest request){
        JSONObject doorPlateElement_toJSON = JSONObject.fromObject(doorPlateOther);
        if(!isDoorplateJsonValid("公众纠错",doorPlateElement_toJSON)){
            throw new BizException("4009","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        Action action = doorplateToAction(doorPlateOther,"公众纠错");
        int actionState = doorPlateService.addAction(action);
        if(actionState == 0){
            //发送消息
            String token = request.getHeader("Token");
            log.info(token);
            com.alibaba.fastjson.JSONObject message = new com.alibaba.fastjson.JSONObject();
            message.put("applicationType","doorplate_4");
            message.put("applicationName","门牌公众纠错");
            message.put("mobile",doorPlateOther.getContact_number());
            message.put("applicationId",action.getGID());
            String res = HttpRequest.post("http://zsdl.myqnapcloud.com:8089/api/process/pushProcess").
                    header("Token", token).
                    body(com.alibaba.fastjson.JSONObject.toJSONString(message)).execute().body();
            log.info(res);
            return ResultResponse.success(doorPlateService.findByGID(action.getGID()));
        }else{
            throw new BizException("4008","客户端请求的参数错误，检查标识码正确性。");
        }
    }


    //判断提交的json字符串的规范性
    private Boolean isJsonValid(JSONObject jsonObject){
        List<String> keylist = Arrays.asList("province", "city", "district", "township", "village", "geom","category");
        for (String key : keylist) {
            if (!jsonObject.get(key).equals("string")&&!jsonObject.get(key).equals("String")&&jsonObject.get(key)!=null && !jsonObject.get(key).equals("")){
                continue;
            }
            return false;
        }
        return true;
    }

    private Action doorplateToAction(Object doorPlateElement, String action){
        Action result = new Action();
        result.setACTION(action);
        result.setCONTENT(new Gson().toJson(doorPlateElement));
        return result;
    }

    //判断提交的json字符串的规范性
    private Boolean isDoorplateJsonValid(String action, Object Object){
        JSONObject jsonObject = JSONObject.fromObject(Object);
        switch (action) {
            case "业务新增":
            case "公众申报":
                List<String> keylist = Arrays.asList("province", "city", "district", "township", "village","geom","category");
                for (String key : keylist) {
                    if (!jsonObject.get(key).equals("string")&&!jsonObject.get(key).equals("String")&&jsonObject.get(key)!=null && !jsonObject.get(key).equals("")){
                        continue;
                    }
                    return false;
                }
                return true;
            case "业务修改":
            case "业务删除":
            case "公众纠错":
                return !jsonObject.get("uid").equals("string")&&!jsonObject.get("uid").equals("String")&&!jsonObject.get("uid").equals("");
        }
        return false;
    }


    //判断提交的json字符串的规范性
    private Boolean isJsonValid(String action, Object Object){
        switch (action) {
            case "业务新增":
            case "公众申报":
                List<String> keyList = Arrays.asList("STANDARD_NAME","TOPONYMIC_CATEGORY","GEOM","ESTABLISHMENT_YEAR","TOPONYM_ORIGIN","TOPONYM_MEANING","TOPONYM_HISTORY","ADMINISTRATIVE_DISTRICT");
                for (String key : keyList) {
                    if (net.sf.json.JSONObject.fromObject(Object).has(key))
                        continue;
                    return false;
                }
                return true;
            case "业务修改":
            case "业务删除":
            case "公众纠错":
                return JSONObject.fromObject(Object).has("IDENTIFICATION_CODE");
        }
        return false;
    }

    private Action toponymToAction(Object toponym, String action){
        Action result = new Action();
        result.setACTION(action);
        result.setCONTENT(new Gson().toJson(toponym));
        return result;
    }

    private void pushProcess(String token,com.alibaba.fastjson.JSONObject message){
        HttpRequest.post("http://zsdl.myqnapcloud.com:8089/api/process/pushProcess").
                header("token", token).
                body(com.alibaba.fastjson.JSONObject.toJSONString(message)).execute().body();
    }
}
