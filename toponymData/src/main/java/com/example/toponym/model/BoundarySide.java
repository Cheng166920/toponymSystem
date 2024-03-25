package com.example.toponym.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
@Data
@Schema(name="BoundarySide对象",description = "相邻行政区划的共边统计对象")
public class BoundarySide implements Serializable {
    @Schema(name="BOUNDARY_NAME",description = "界线名称")
    private String BOUNDARY_NAME;
    @Schema(name="COMMON_SIDE_LENGTH",description = "共边长度")
    private double COMMON_SIDE_LENGTH;
}
