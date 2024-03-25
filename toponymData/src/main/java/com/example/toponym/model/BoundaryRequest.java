package com.example.toponym.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class BoundaryRequest implements Serializable {
    @Schema(name = "GID", description = "界线申领序号")
    @JsonProperty("GID")private Integer GID;

    @Schema(name = "CODE", description = "界线代码")
    @JsonProperty("CODE") private String CODE;

    @Schema(name = "TYPE", description = "数据类型")
    @JsonProperty("TYPE") private String TYPE;

    @Schema(name = "GEOM", description = "几何要素")
    @JsonProperty("GEOM") private String GEOM;

    @Schema(name = "REVIEW", description = "审核状态，其阈值为：未审核、审核通过、审核未通过。")
    @JsonProperty("REVIEW") private String REVIEW;

    @Schema(name = "USER", description = "用户名称")
    @JsonProperty("USER") private String USER;
}
