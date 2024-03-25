package com.example.toponym.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.sf.json.JSONObject;

import java.io.Serializable;

@Data
@Schema(name = "Action对象", description = "生产数据表对象")
public class Action implements Serializable{

    @Schema(name = "GID", description = "生产数据表序号")
    @JsonProperty("GID")private Integer GID;

    @Schema(name = "CONTENT", description = "需要修改的数据")
    @JsonProperty("CONTENT") private Object CONTENT;

    @Schema(name = "ACTION", description = "需要进行的操作，其阈值为：业务新增、业务删除、业务修改、公众申报、公众纠错。")
    @JsonProperty("ACTION") private String ACTION;

    @Schema(name = "ACTION_STATUS", description = "操作完成状态，其阈值为：未完成、已完成、异常。")
    @JsonProperty("ACTION_STATUS") private String ACTION_STATUS;

    @Schema(name = "REVIEW", description = "审核状态，其阈值为：未审核、审核通过、审核未通过。")
    @JsonProperty("REVIEW") private String REVIEW;
}
