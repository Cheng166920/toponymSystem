package com.example.toponym.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Data
@Schema(name="Category",description = "基于行政区划的地名类别统计对象")
public class Category implements Serializable {
    @Schema(name="ID",description = "序号")
    private Integer ID;

    @Schema(name="CODE",description = "地名大类代码")
    private String CODE;

    @Schema(name="LARGE_CATEGORY",description = "地名大类")
    private String LARGE_CATEGORY;

    @Schema(name="STAT_NUMBER",description = "数量")
    private Integer STAT_NUMBER;

}
