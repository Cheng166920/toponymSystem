package com.example.toponym.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name="BoundaryCategory对象",description = "基于行政等级勘界统计对象")
public class BoundaryCategory implements Serializable{
    @Schema(name="CATEGORY",description = "行政级别")
    private String CATEGORY;
    @Schema(name="BOUNDARY_STONE_NUM",description = "界桩数量")
    private Long BOUNDARY_STONE_NUM;
    @Schema(name="BOUNDARY_LENGTH",description = "界线长度")
    private double BOUNDARY_LENGTH;
    @Schema(name="TRIPLE_INTERSECTION_NUM",description = "三交点数量")
    private Long TRIPLE_INTERSECTION_NUM;
    @Schema(name="BOUNDARY_POINT_NUM",description = "边界点数量")
    private Long BOUNDARY_POINT_NUM;

}
