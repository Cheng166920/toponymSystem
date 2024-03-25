package com.example.toponym.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.sf.json.JSONArray;

import java.io.Serializable;
@Data
@Schema(name = "DoorPlateHistory对象",description = "门牌号历史回溯表对象")
public class DoorPlateHistory implements Serializable{
    @Schema(name = "uid",description = "地址的唯一标识码")
    private String uid;
    @Schema(name = "change_jsonb",description = "门牌变更对比记录，jsonArray格式")
    private Object change_jsonb;
    @Schema(name = "change_time",description = "发生变更的时间")
    private String change_time;
}

