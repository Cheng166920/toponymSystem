package com.example.toponym.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Data
@Schema(name = "Toponym对象", description = "新增地名对象")
public class Toponym implements Serializable {
    @Schema(name = "IDENTIFICATION_CODE", description = "标识码")
    @JsonProperty("IDENTIFICATION_CODE")private String IDENTIFICATION_CODE;

    @Schema(name = "STANDARD_NAME", description = "标准名称")
    @JsonProperty("STANDARD_NAME")private String STANDARD_NAME;

    @Schema(name = "TOPONYMIC_CATEGORY", description = "地名类别")
    @JsonProperty("TOPONYMIC_CATEGORY")private String TOPONYMIC_CATEGORY;

    @Schema(name = "GEOM", description = "地点位置")
    @JsonProperty("GEOM")private Object GEOM;

    @Schema(name = "ESTABLISHMENT_YEAR", description = "设立年份")
    @JsonProperty("ESTABLISHMENT_YEAR")private String ESTABLISHMENT_YEAR;

    @Schema(name = "TOPONYM_ORIGIN", description = "地名的来历")
    @JsonProperty("TOPONYM_ORIGIN")private String TOPONYM_ORIGIN;

    @Schema(name = "TOPONYM_MEANING", description = "地名的含义")
    @JsonProperty("TOPONYM_MEANING")private String TOPONYM_MEANING;

    @Schema(name = "TOPONYM_HISTORY", description = "地名的历史沿革")
    @JsonProperty("TOPONYM_HISTORY")private String TOPONYM_HISTORY;

    @Schema(name = "ADMINISTRATIVE_DISTRICT", description = "所跨行政区")
    @JsonProperty("ADMINISTRATIVE_DISTRICT")private String ADMINISTRATIVE_DISTRICT;

    @Schema(name = "REMARK", description = "备注")
    @JsonProperty("REMARK")private String REMARK;

    @Schema(name = "USER", description = "申请用户")
    @JsonProperty("USER")private String USER;

    @Schema(name = "CONTACT_NUMBER", description = "联系电话")
    @JsonProperty("CONTACT_NUMBER")private String CONTACT_NUMBER;

    @Schema(name = "ORGANIZATION", description = "组织")
    @JsonProperty("ORGANIZATION")private String ORGANIZATION;


}
