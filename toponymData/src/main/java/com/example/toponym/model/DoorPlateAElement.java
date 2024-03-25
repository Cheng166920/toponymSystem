package com.example.toponym.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "DoorPlateAElement对象",description = "门牌号应用数据表对象")
public class DoorPlateAElement implements Serializable {
    @Schema(name = "uid",description = "地址的唯一标识码")
    private String uid;
    @Schema(name = "fts_word",description = "全文检索词语分词结果")
    private String fts_word;
    @Schema(name = "fts_char",description = "全文检索单字分词结果")
    private String fts_char;
}
