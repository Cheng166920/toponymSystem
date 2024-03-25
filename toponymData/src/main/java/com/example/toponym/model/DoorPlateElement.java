package com.example.toponym.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "DoorPlateElement对象",description = "门牌号数据表对象")
public class DoorPlateElement implements Serializable {
    @Schema(name = "uid",description = "地址的唯一标识码")
    private String uid;
    @Schema(name = "province",description = "省份")
    private String province;
    @Schema(name = "city",description = "市级")
    private String city;
    @Schema(name = "district",description = "区县级")
    private String district;
    @Schema(name = "township",description = "乡镇级")
    private String township;
    @Schema(name = "village",description = "村/社区")
    private String village;
    @Schema(name = "road",description = "道路")
    private String road;
    @Schema(name = "road_direction",description = "道路方位走向")
    private String road_direction;
    @Schema(name = "estate",description = "小区")
    private String estate;
    @Schema(name = "building",description = "楼栋")
    private String building;
    @Schema(name = "unit",description = "单元")
    private String unit;
    @Schema(name = "doorplate",description = "门牌号码")
    private String doorplate;
    @Schema(name = "doorplate_sub",description = "门牌号码附号")
    private String doorplate_sub;
    @Schema(name = "room",description = "房间号")
    private String room;
    @Schema(name = "house_license",description = "门牌证号")
    private String house_license;
    @Schema(name = "license_time",description = "制证时间")
    private String license_time;
    @Schema(name = "category",description = "门牌类别")
    private String category;
    @Schema(name = "owner",description = "产权人")
    private String owner;
    @Schema(name = "owner_phone",description = "产权人联系电话")
    private String owner_phone;
    @Schema(name = "construction",description = "建筑物名称")
    private String construction;
    @Schema(name = "principal",description = "负责人")
    private String principal;
    @Schema(name = "principal_phone",description = "负责人联系电话")
    private String principal_phone;
    @Schema(name = "collect_time",description = "采集时间")
    private String collect_time;
    @Schema(name = "linear_toponym",description = "关联线状地名")
    private String linear_toponym;
    @Schema(name = "point_toponym",description = "关联点状地名")
    private String point_toponym;
    @Schema(name = "geom",description = "地理坐标Point")
    private Object geom;
    @Schema(name = "remarks",description = "备注")
    private String remarks;
    @Schema(name = "photo_name",description = "照片文件名")
    private String photo_name;

}
