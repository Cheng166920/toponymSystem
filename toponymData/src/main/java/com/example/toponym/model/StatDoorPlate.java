package com.example.toponym.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
@Data
@Schema(name="StatDoorPlate对象",description = "门牌地址统计对象")
public class StatDoorPlate implements Serializable{
    @Schema(name="ROAD",description = "道路")
    private String ROAD;
    @Schema(name="STAT_DOORPLATE",description = "门牌数量")
    private Long STAT_DOORPLATE;
}
