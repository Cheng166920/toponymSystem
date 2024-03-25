package com.example.toponym.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Data
@Schema(name = "PointExtent对象", description = "地名空间数据表对象")
public class PointExtent implements Serializable {

    @Schema(name = "GID", description = "自增序号")
    private Long GID;

    @Schema(name = "IDENTIFICATION_CODE", description = "标识码")
    private String IDENTIFICATION_CODE;//标识码

    @Schema(name = "DIAGRAM_NAME", description = "图上名称")
    private String DIAGRAM_NAME;//图上名称

    @Schema(name = "CATEGORY_CODE", description = "类别代码")
    private String CATEGORY_CODE;//类别代码

    @Schema(name = "USE_TIME", description = "使用时间")
    private String USE_TIME;//使用时间

    /**
     * 空间信息转换为字符串
     */
    @Schema(name = "GEOM", description = "几何要素")
    private String GEOM;
   // private String POLYGON;
}
