package com.example.toponym.controller;


import com.example.toponym.common.BizException;
import com.example.toponym.model.*;
import com.example.toponym.service.DoorPlateService;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@Tag(name = "门牌管理模块",description = "门牌的增删改操作")

@RequestMapping("/doorplate")
public class DoorPlateActionController {
    @Autowired
    private DoorPlateService doorPlateService;

    @Operation(summary = "管理员：门牌添加，管理员将新增数据写入数据库")
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
            @Parameter(name = "category",description = "门牌类别，可选【商户、住宅、机关企事业单位、企业、商住、单位、其他】，必填",example = "商户"),
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
            @Parameter(name = "geom",description = "地理坐标Point,swagger中提交需要引号和转译符，必填",example = "{\"type\":\"Point\",\"coordinates\":[112.160426843,30.337650579]}")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "门牌号表数据结构，请输入需要添加的门牌信息，uid不需要提交，空值提交null。五级行政区划（province、city、district、township、village）必填，用于生成uid",required = true)
    @PostMapping("/add")
    public ResultResponse addDoorplate(@RequestBody DoorPlateElement doorPlateElement){
        JSONObject doorPlateElement_toJSON = JSONObject.fromObject(doorPlateElement);
        if(!isJsonValid(doorPlateElement_toJSON) || doorPlateService.getCityCode(doorPlateElement) == null){
            throw new BizException("4009","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        String resultCode = doorPlateService.addDoorplate(doorPlateElement);
        if(!resultCode.equals("0")){
            String message = "客户端请求的参数错误，数据库已有该数据，uid为：" + resultCode;
            throw new BizException("4007",message);
        }
        String uid = doorPlateElement.getUid();
        return ResultResponse.success(doorPlateService.findById(uid));
    }

    @Operation(summary = "提交审批：门牌添加，将新增数据写入审核表")
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
            @Parameter(name = "house_license",description = "门牌证号，需唯一，必填",example = "421003103000999"),
            @Parameter(name = "license_time",description = "制证时间",example = "2023-05-20 00:00:00"),
            @Parameter(name = "category",description = "门牌类别，可选【商户、住宅、机关企事业单位、企业、商住、单位、其他】，必填",example = "商户"),
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
            @Parameter(name = "geom",description = "地理坐标Point,swagger中提交需要引号和转译符，必填",example = "{\"type\":\"Point\",\"coordinates\":[112.160426843,30.337650579]}")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "门牌号表数据结构，请输入需要添加的门牌信息，uid不需要提交。五级行政区划（province、city、district、township、village）必填，用于生成uid",required = true)
    @PostMapping("/check/add")
    public ResultResponse addCheckDoorplate(@RequestBody DoorPlateElement doorPlateElement){
        JSONObject doorPlateElement_toJSON = JSONObject.fromObject(doorPlateElement);
        if(!isJsonValid(doorPlateElement_toJSON) || doorPlateService.getCityCode(doorPlateElement) == null){
            throw new BizException("4009","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        Action action = doorplateToAction(doorPlateElement,"业务新增");
        int actionState = doorPlateService.addAction(action);
        if(actionState != 0){
            throw new BizException("4007","客户端请求的参数错误，数据库已有该门牌证号");
        }
        return ResultResponse.success(doorPlateService.findByGID(action.getGID()));
    }

    @Operation(summary = "管理员：门牌属性变更，管理员用修改数据更新数据库")
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
            @Parameter(name = "geom",description = "地理坐标Point,swagger中提交需要引号和转译符",example = "{\"type\":\"Point\",\"coordinates\":[112.160426843,30.337650579]}")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "门牌号表数据结构，请输入需要修改的门牌信息，uid必填，五级行政区划（province、city、district、township、village）默认不做修改",required = true)
    @PostMapping("/update")
    public ResultResponse updateDoorplate(@RequestBody DoorPlateElement doorPlateElement){
        int actionState = doorPlateService.updateDoorplate(doorPlateElement);
        if(actionState != 0){
            throw new BizException("4008","客户端请求的参数错误，检查标识码正确性。");
        }
        String uid = doorPlateElement.getUid();
        return ResultResponse.success(doorPlateService.findById(uid));
    }

    @Operation(summary = "提交审批：门牌属性变更，将需要修改的数据写入审核表")
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
            @Parameter(name = "geom",description = "地理坐标Point,swagger中提交需要引号和转译符",example = "{\"type\":\"Point\",\"coordinates\":[112.160426843,30.337650579]}")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "门牌号表数据结构，请输入需要修改的门牌信息，uid必填，五级行政区划（province、city、district、township、village）默认不做修改",required = true)
    @PostMapping("/check/update")
    public ResultResponse updateCheckDoorplate(@RequestBody DoorPlateElement doorPlateElement){
        JSONObject doorPlateElement_toJSON = JSONObject.fromObject(doorPlateElement);
        if(!isJsonValid("业务修改",doorPlateElement_toJSON)){
            throw new BizException("4009","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        Action action = doorplateToAction(doorPlateElement,"业务修改");
        int actionState = doorPlateService.addAction(action);
        if(actionState != 0){
            throw new BizException("4008","客户端请求的参数错误，检查标识码正确性。");
        }
        return ResultResponse.success(doorPlateService.findByGID(action.getGID()));

    }

    //删除门牌
    @Operation(summary = "管理员：门牌删除，管理员删除门牌数据库中对应ID的门牌")
    @Parameters({
            @Parameter(name = "uid",description = "需要删除地址的唯一标识码（必须）")
    })
    @GetMapping("/delete")
    public ResultResponse deleteDoorplate(@Parameter(description = "门牌号标识码")String uid){
        int actionState = doorPlateService.deleteDoorplate(uid);
        if(actionState == 0){
            throw new BizException("4008","客户端请求的参数错误，检查标识码正确性。");
        }
        return ResultResponse.success(true);
    }

    //删除门牌
    @Operation(summary = "提交审批：门牌删除，将需要删除的门牌写入审核表")
    @Parameters({
            @Parameter(name = "uid",description = "需要删除地址的唯一标识码（必须）")
    })
    @GetMapping("/check/delete")
    public ResultResponse deleteCheckDoorplate(@Parameter(description = "门牌号标识码")String uid){
        JSONObject json = new JSONObject();
        json.put("uid",uid);
        Action action = doorplateToAction(json,"业务删除");
        int actionState = doorPlateService.addAction(action);
        if(actionState != 0){
            throw new BizException("4008","客户端请求的参数错误，检查标识码正确性。");
        }
        return ResultResponse.success(doorPlateService.findByGID(action.getGID()));
    }

    //删除生产表数据
    @Operation(summary = "审核员：删除审核表数据")
    @GetMapping("/check/deleteMessage")
    public ResultResponse deleteMessage(@Parameter(description = "数据库自增序号") Integer gid){
        doorPlateService.deleteAction(gid);
        return ResultResponse.success();
    }

    //将操作数据写入生产数据库
    @Operation(summary = "提交审批：门牌纠错，将公众纠错数据写入审核表")
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
            @Parameter(name = "geom",description = "地理坐标Point,swagger中提交需要引号和转译符",example = "{\"type\":\"Point\",\"coordinates\":[112.160426843,30.337650579]}"),
            @Parameter(name = "contact_number",description = "公众提交的电话",example = "13677778888")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "门牌号表数据结构，请输入需要写入的数据内容。uid必填，五级行政区划（province、city、district、township、village）默认不做修改",required = true)
    @PostMapping("/action")
    public ResultResponse addAction(@RequestBody DoorPlateOther doorPlateOther){
        JSONObject doorPlateElement_toJSON = JSONObject.fromObject(doorPlateOther);
        if(!isJsonValid("公众纠错",doorPlateElement_toJSON)){
            throw new BizException("4009","客户端请求的参数错误，检查提交JSON格式的正确性。");
        }
        Action action = doorplateToAction(doorPlateOther,"公众纠错");
        int actionState = doorPlateService.addAction(action);
        if(actionState == 0){
            return ResultResponse.success(doorPlateService.findByGID(action.getGID()));
        }else{
            throw new BizException("4008","客户端请求的参数错误，检查标识码正确性。");
        }
    }

    //修改生产表审核状态
    @Operation(summary = "审核员：门牌审核，修改审核状态")
    @Parameters({
            @Parameter(name = "review",description = "审核后完成状态,其阈值为：审核通过、审核不通过"),
            @Parameter(name = "gid",description = "生产表自增序号")
    })
    @GetMapping("/review")
    public ResultResponse updateReview(String review,int gid){
        if(!review.equals("审核通过")&&!review.equals("审核不通过")){
            throw new BizException("4010","客户端请求的参数错误，检查提交参数的正确性。");
        }
        doorPlateService.updateReview(review,gid);

        return ResultResponse.success(doorPlateService.findByGID(gid));
    }

    //更新生产表数据
    @Operation(summary = "审核员：修改审核表数据")
    @Parameters({
            @Parameter(name = "GID",description = "生产表自增序号",example = "1"),
            @Parameter(name = "CONTENT",description = "管理员对审核表修改后的完整数据，json格式，需要提交结构与DoorPlateElement门牌号表数据结构一致"),
            @Parameter(name = "ACTION",description = "进行的操作，其阈值为：业务新增、业务删除、业务修改、公众申报、公众纠错",example = "业务新增")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "地址生产表数据结构，请输入需要更新的数据的序号和操作，以及该数据更新后的内容以json形式放入content中。",required = true)
    @PostMapping("/check")
    public ResultResponse updateAction(@RequestBody Action action_list){
        if(isJsonValid(action_list.getACTION(), action_list.getCONTENT())){
            int actionState = doorPlateService.updateAction(action_list);
            if(actionState == 0){
                return ResultResponse.success(doorPlateService.findByGID(action_list.getGID()));
            }else{
                return ResultResponse.error("4010","客户端请求的参数错误，检查提交参数的正确性。");
            }
        }
        throw new BizException("4009","客户端请求的参数错误，检查提交JSON格式的正确性。");

    }

    //查看生产表数据
    @GetMapping("/check/list")
    @Operation(summary = "审核员：查看所有门牌审核数据")
    @Parameters({
            @Parameter(name = "currentPage",description = "当前页"),
            @Parameter(name = "pageSize",description = "页面大小")
    })
    public ResultResponse findAll(@RequestParam(name = "currentPage",defaultValue = "1")int currentPage,
                                  @RequestParam(name = "pageSize",defaultValue = "20")int pageSize){
        PageHelper.startPage(currentPage,pageSize);
        List<Action> actionList = doorPlateService.findAll();
        return ResultResponse.success(new PageBean<>(actionList));
    }

    //查看单条生产表数据
    @GetMapping("/check/listById")
    @Operation(summary = "审核员：查看单条门牌审核数据")
    @Parameters({
            @Parameter(name = "gid",description = "生产表自增序号,未审核情况下：当操作为修改或删除时候，返回数据前后对比值。旧值为：current_version；待审核值为：modify，" +
                    "当操作为新增时，只有：modify；已审核情况下，modify中返回完整的门牌审核数据结构")
    })
    public ResultResponse findByGID(int gid){
        Action action = doorPlateService.findByGID(gid);
        JSONObject input_content = JSONObject.fromObject(action.getCONTENT());
        String input_action = action.getACTION();
        String review = action.getREVIEW();
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        result.put("modify",action);
        if(review.equals("未审核")){
            if(!input_action.equals("业务新增") && !input_action.equals("公众申报")){
                DoorPlateElement doorPlateElement = doorPlateService.findById(input_content.getString("uid")).get(0);
                result.put("current_version",doorPlateElement);
            }else{
                result.put("current_version",null);
            }
        }else{
            result.put("current_version",null);
        }

        return ResultResponse.success(result);
    }

    @Operation(summary = "获取五级行政区划树形结构")
    @GetMapping("/check/city_tree")
    public ResultResponse getCityTree(){
        List<Map<String,Object>> list = doorPlateService.getCityTree();
        return ResultResponse.success(list);
    }

    @Operation(summary = "获取单级行政区划")
    @GetMapping("/get_district")
    @Parameters({
            @Parameter(name = "pid",description = "行政区划代码，缺省值为0时获取省级行政区代码，该接口仅提供湖北省内五级行政区结构，根据提交的行政区代码获取下一级行政区名称及代码")
    })
    public ResultResponse getDistrict(@RequestParam(name = "pid",defaultValue = "0")String pid){
        List<Map<String,String>> list = doorPlateService.getDistrict(pid);
        return ResultResponse.success(list);
    }

    //判断提交的json字符串的规范性
    private Boolean isJsonValid(String action, Object Object){
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
    private Boolean isJsonValid(JSONObject jsonObject){
        List<String> keylist = Arrays.asList("province", "city", "district", "township", "village","geom","category");
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
}
