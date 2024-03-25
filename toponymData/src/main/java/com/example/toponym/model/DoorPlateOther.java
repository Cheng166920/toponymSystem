package com.example.toponym.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "DoorPlateOther对象",description = "门牌号补充数据对象")
public class DoorPlateOther extends DoorPlateElement {
    @Schema(name = "contact_number",description = "公众提交的电话")
    private String contact_number;
}
