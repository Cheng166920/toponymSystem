package com.example.toponym.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name="AdminTpm对象",description = "基于行政区划的地名类别统计对象")
public class AdminTpm implements Serializable {

    @Schema(name="ADMIN_NAME",description = "行政区")
    @JsonProperty("ADMIN_NAME")private String ADMIN_NAME;

    @Schema(name="CODE",description = "地名代码")
    @JsonProperty("CODE")private String CODE;

    @Schema(name="RANK",description = "行政级别")
    @JsonProperty("RANK")private String RANK;

    @Schema(name="STAT_TOTAL",description = "地名数量")
    @JsonProperty("STAT_TOTAL")private Long STAT_TOTAL;

    @Schema(name="STAT_AREA",description = "面积")
    @JsonProperty("STAT_AREA")private String STAT_AREA;

    @Schema(name="STAT_POPULATION",description = "人口数量")
    @JsonProperty("STAT_POPULATION")private String STAT_POPULATION;

    @Schema(name="STAT_PUMPING_STATION",description = "泵站类地名数量")
    @JsonProperty("STAT_PUMPING_STATION")private Long STAT_PUMPING_STATION;

    @Schema(name="STAT_WALL",description = "城墙堡类地名数量")
    @JsonProperty("STAT_WALL")private Long STAT_WALL;

    @Schema(name="STAT_POND",description = "池塘海塘类地名数量")
    @JsonProperty("STAT_POND") private Long STAT_POND;

    @Schema(name="STAT_SHIP_STATION",description = "船闸升船机站类地名数量")
    @JsonProperty("STAT_SHIP_STATION")private Long STAT_SHIP_STATION;

    @Schema(name="STAT_UNIT",description = "单位类地名数量")
    @JsonProperty("STAT_UNIT")private Long STAT_UNIT;

    @Schema(name="STAT_DAOBAN",description = "道班类地名数量")
    @JsonProperty("STAT_DAOBAN")private Long STAT_DAOBAN;

    @Schema(name="STAT_ROAD",description = "道路街巷类地名数量")
    @JsonProperty("STAT_ROAD")private Long STAT_ROAD;

    @Schema(name="STAT_SIGN",description = "地名标志类地名数量")
    @JsonProperty("STAT_SIGN")private Long STAT_SIGN;

    @Schema(name="STAT_LAND",description = "地片区片类地名数量")
    @JsonProperty("STAT_LAND")private Long STAT_LAND;

    @Schema(name="STAT_AQUEDUCT",description = "渡槽类地名数量")
    @JsonProperty("STAT_AQUEDUCT")private Long STAT_AQUEDUCT;

    @Schema(name="STAT_FERRY",description = "渡口类地名数量")
    @JsonProperty("STAT_FERRY")private Long STAT_FERRY;

    @Schema(name="STAT_POWER_STATION",description = "发电站类地名数量")
    @JsonProperty("STAT_POWER_STATION")private Long STAT_POWER_STATION;
    @Schema(name="STAT_HOUSE",description = "房屋类地名数量")
    @JsonProperty("STAT_HOUSE") private Long STAT_HOUSE;
    @Schema(name="STAT_DEVELOPMENT_ZONE",description = "工业开发区类地名数量")
    @JsonProperty("STAT_DEVELOPMENT_ZONE")private Long STAT_DEVELOPMENT_ZONE;
    @Schema(name="STAT_TRANSPORTATION_STOP",description = "公共交通车站类地名数量")
    @JsonProperty("STAT_TRANSPORTATION_STOP")private Long STAT_TRANSPORTATION_STOP;
    @Schema(name="STAT_HIGHWAY",description = "公路类地名数量")
    @JsonProperty("STAT_HIGHWAY")private Long STAT_HIGHWAY;
    @Schema(name="STAT_PARK",description = "公园风景区类地名数量")
    @JsonProperty("STAT_PARK")private Long STAT_PARK;
    @Schema(name="STAT_PIPELINE",description = "管道类地名数量")
    @JsonProperty("STAT_PIPELINE")private Long STAT_PIPELINE;
    @Schema(name="STAT_IRRIGATION_CANAL",description = "灌溉渠类地名数量")
    @JsonProperty("STAT_IRRIGATION_CANAL") private Long STAT_IRRIGATION_CANAL;
    @Schema(name="STAT_STADIUM",description = "广场体育场类地名数量")
    @JsonProperty("STAT_STADIUM")private Long STAT_STADIUM;
    @Schema(name="STAT_SEAPORT",description = "海港河港类地名数量")
    @JsonProperty("STAT_SEAPORT")private Long STAT_SEAPORT;
    @Schema(name="STAT_CULVERT",description = "涵洞类地名数量")
    @JsonProperty("STAT_CULVERT")private Long STAT_CULVERT;
    @Schema(name="STAT_EMBANKMENT",description = "河堤湖堤闸坝拦河坝类地名数量")
    @JsonProperty("STAT_EMBANKMENT")private Long STAT_EMBANKMENT;
    @Schema(name="STAT_RIVER",description = "河流类地名数量")
    @JsonProperty("STAT_RIVER")private Long STAT_RIVER;
    @Schema(name="STAT_LAKE",description = "湖泊类地名数量")
    @JsonProperty("STAT_LAKE")private Long STAT_LAKE;
    @Schema(name="STAT_ROUNDABOUT",description = "环岛路口类地名数量")
    @JsonProperty("STAT_ROUNDABOUT")private Long STAT_ROUNDABOUT;
    @Schema(name="STAT_RAILWAY_STATION",description = "火车站类地名数量")
    @JsonProperty("STAT_RAILWAY_STATION")private Long STAT_RAILWAY_STATION;
    @Schema(name="STAT_TOURIST_ATTRACTION",description = "纪念地旅游景点类地名数量")
    @JsonProperty("STAT_TOURIST_ATTRACTION")private Long STAT_TOURIST_ATTRACTION;
    @Schema(name="STAT_GAS_STATION",description = "加油站类地名数量")
    @JsonProperty("STAT_GAS_STATION")private Long STAT_GAS_STATION;
    @Schema(name="STAT_WELL",description = "井类地名数量")
    @JsonProperty("STAT_WELL")private Long STAT_WELL;
    @Schema(name="STAT_SETTLEMENT",description = "居民点类地名数量")
    @JsonProperty("STAT_SETTLEMENT")private Long STAT_SETTLEMENT;
    @Schema(name="STAT_AGRICULTURE_AREA",description = "农林牧渔区类地名数量")
    @JsonProperty("STAT_AGRICULTURE_AREA")private Long STAT_AGRICULTURE_AREA;
    @Schema(name="STAT_GUTTER",description = "排水沟类地名数量")
    @JsonProperty("STAT_GUTTER")private Long STAT_GUTTER;
    @Schema(name="STAT_BRIDGE",description = "桥梁类地名数量")
    @JsonProperty("STAT_BRIDGE")private Long STAT_BRIDGE;
    @Schema(name="STAT_SPRING",description = "泉类地名数量")
    @JsonProperty("STAT_SPRING")private Long STAT_SPRING;
    @Schema(name="STAT_MASS_ORGANIZATION",description = "群众自治组织类地名数量")
    @JsonProperty("STAT_MASS_ORGANIZATION")private Long STAT_MASS_ORGANIZATION;
    @Schema(name="STAT_MEMORIAL_SITE",description = "人物事件纪念地类地名数量")
    @JsonProperty("STAT_MEMORIAL_SITE")private Long STAT_MEMORIAL_SITE;
    @Schema(name="STAT_FOREST",description = "森林类地名数量")
    @JsonProperty("STAT_FOREST")private Long STAT_FOREST;
    @Schema(name="STAT_MOUNTAIN",description = "山类地名数量")
    @JsonProperty("STAT_MOUNTAIN")private Long STAT_MOUNTAIN;
    @Schema(name="STAT_HILLOCK",description = "山体类地名数量")
    @JsonProperty("STAT_HILLOCK")private Long STAT_HILLOCK;
    @Schema(name="STAT_TRANSMISSION_SUBSTATION",description = "输变电站类地名数量")
    @JsonProperty("STAT_TRANSMISSION_SUBSTATION")private Long STAT_TRANSMISSION_SUBSTATION;
    @Schema(name="STAT_RESERVOIR",description = "水库类地名数量")
    @JsonProperty("STAT_RESERVOIR")private Long STAT_RESERVOIR;
    @Schema(name="STAT_RAILWAY",description = "铁路类地名数量")
    @JsonProperty("STAT_RAILWAY")private Long STAT_RAILWAY;
    @Schema(name="STAT_PAVILION",description = "亭台碑塔类地名数量")
    @JsonProperty("STAT_PAVILION")private Long STAT_PAVILION;
    @Schema(name="STAT_PARKING_LOT",description = "停车场类地名数量")
    @JsonProperty("STAT_PARKING_LO")private Long STAT_PARKING_LOT;
    @Schema(name="STAT_FLOOD_AREA",description = "蓄洪区泻洪区类地名数量")
    @JsonProperty("STAT_FLOOD_AREA")private Long STAT_FLOOD_AREA;
    @Schema(name="STAT_CANAL",description = "运河类地名数量")
    @JsonProperty("STAT_CANAL")private Long STAT_CANAL;
    @Schema(name="STAT_COACH_STATION",description = "长途汽车站类地名数量")
    @JsonProperty("STAT_COACH_STATION")private Long STAT_COACH_STATION;
    @Schema(name="STAT_ISLAND",description = "洲河岛湖岛矶类地名数量")
    @JsonProperty("STAT_ISLAND")private Long STAT_ISLAND;
    @Schema(name="STAT_NATURE_RESERVE",description = "自然保护区类地名数量")
    @JsonProperty("STAT_NATURE_RESERVE")private Long STAT_NATURE_RESERVE;
    @Schema(name="STAT_RELIGIOUS_MONUMENT",description = "宗教纪念地类地名数量")
    @JsonProperty("STAT_RELIGIOUS_MONUMENT")private Long STAT_RELIGIOUS_MONUMENT;
    @Schema(name="STAT_IRRIGATION_DISTRICT",description = "灌区类地名数量")
    @JsonProperty("STAT_IRRIGATION_DISTRICT")private Long STAT_IRRIGATION_DISTRICT;
}