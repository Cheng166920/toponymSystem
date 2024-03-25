package com.example.toponym.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
@Data
@Schema(name="BoundaryAdmin对象",description = "基于行政区划勘界统计对象")
public class BoundaryAdmin implements Serializable{
    @Schema(name="ADMIN",description = "行政区")
    private String ADMIN;
    @Schema(name="AREA",description = "行政区面积")
    private double AREA;
    @Schema(name="BOUNDARY_LENGTH",description = "村级界线长度")
    private double BOUNDARY_LENGTH;
    @Schema(name="VILLAGE_NUM",description = "村庄数量")
    private Long VILLAGE_NUM;
    @Schema(name="BOUNDARY_STONE_NUM",description = "边界点数量")
    private Long BOUNDARY_STONE_NUM;
    @Schema(name="COMMON_SIDE_LENGTH",description = "边界长度")
    private double COMMON_SIDE_LENGTH;

}
