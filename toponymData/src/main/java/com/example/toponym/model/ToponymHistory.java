package com.example.toponym.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "ToponymHistory对象", description = "地名历史对象")
public class ToponymHistory implements Serializable {

    @Schema(name = "ID", description = "自增序号")
    private Long ID;

    @Schema(name = "IDENTIFICATION_CODE", description = "地名空间标识码")
    private String IDENTIFICATION_CODE;

    @Schema(name = "NEW_CONTENT", description = "变更后的记录")
    private Object NEW_CONTENT;

    @Schema(name = "OLD_CONTENT", description = "变更前的记录")
    private Object OLD_CONTENT;

    @Schema(name = "UPDATE_ACTION",description = "发生变更的操作,其阈值为：业务新增、业务删除、业务修改、公众申报、公众纠错。")
    private String UPDATE_ACTION;
}
