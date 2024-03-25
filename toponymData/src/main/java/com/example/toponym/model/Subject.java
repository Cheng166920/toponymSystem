package com.example.toponym.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name="Subject对象",description = "地名专题统计对象")
public class Subject implements Serializable {
    @Schema(name = "STAT_ID", description = "统计指标编号")
    private Long STAT_ID;

    @Schema(name = "STAT_SUB_NAME", description = "统计专题名称")
    private String STAT_SUB_NAME;

    @Schema(name = "STAT_CONTENT", description = "统计专题内容")
    private Object STAT_CONTENT;
}
