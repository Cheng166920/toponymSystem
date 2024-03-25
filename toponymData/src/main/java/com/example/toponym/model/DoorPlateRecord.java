package com.example.toponym.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Schema(name = "DoorPlateRecord对象",description = "门牌号历史详细对象")
public class DoorPlateRecord implements Serializable {
//    @Schema(name = "id",description = "历史回溯表自增id")
//    private int id;
    @Schema(name = "uid",description = "地址的唯一标识码")
    private String uid;
    @Schema(name = "old_value",description = "变更前的旧的门牌记录，json格式")
    private Object old_value;
    @Schema(name = "new_value",description = "变更后的新的门牌记录，json格式")
    private Object new_value;
    @Schema(name = "change_time",description = "发生变更的时间")
    private String change_time;
    @Schema(name = "change_action",description = "发生变更的操作，阈值为：insert、update、delete")
    private String change_action;

}
