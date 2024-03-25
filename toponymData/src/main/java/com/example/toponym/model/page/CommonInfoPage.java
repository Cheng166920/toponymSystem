package com.example.toponym.model.page;

import com.example.toponym.model.CommonInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.sf.json.JSONObject;

import java.io.Serializable;

@Data
@Schema(name = "CommonInfoPage对象", description = "地名应用数据表分页对象")
public class CommonInfoPage implements Serializable {

    @Schema(name = "commonInfo", description = "地名应用数据表对象",defaultValue = "{\n" +
                            "  \"DIAGRAM_NAME\": \"庙湖\",\n" +
                            "  \"TOPONYMIC_CATEGORY\": \"湖泊\"\n" +
                            "}")
    private CommonInfo commonInfo;

    @Schema(name = "currentPage", description = "当前页",defaultValue = "1")
    private int currentPage;

    @Schema(name = "pageSize", description = "页面大小",defaultValue = "20")
    private int pageSize;
}
