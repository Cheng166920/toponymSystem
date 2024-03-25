package com.example.toponym.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.sf.json.JSONObject;

import java.io.Serializable;

@Data
@Schema(name = "CommonInfo对象", description = "地名应用数据表对象")
public class CommonInfo implements Serializable {

    @Schema(name = "IDENTIFICATION_CODE", description = "标识码")
    @JsonProperty("IDENTIFICATION_CODE")private String IDENTIFICATION_CODE;

    @Schema(name = "DIAGRAM_NAME", description = "图上名称")
    @JsonProperty("DIAGRAM_NAME")private String DIAGRAM_NAME;

    @Schema(name = "USE_TIME", description = "使用时间")
    @JsonProperty("USE_TIME")private String USE_TIME;

    @Schema(name = "TOPONYM_CODE", description = "地名代码")
    @JsonProperty("TOPONYM_CODE")private String TOPONYM_CODE;

    @Schema(name = "GEOM", description = "几何要素")
    @JsonProperty("GEOM")private Object GEOM;

    @Schema(name = "STANDARD_NAME", description = "标准名称")
    @JsonProperty("STANDARD_NAME")private String STANDARD_NAME;

    @Schema(name = "ALIAS", description = "别名")
    @JsonProperty("ALIAS")private String ALIAS;

    @Schema(name = "ABBREVIATION", description = "简称")
    @JsonProperty("ABBREVIATION")private String ABBREVIATION;

    @Schema(name = "FORMER_NAME", description = "曾用名")
    @JsonProperty("FORMER_NAME")private String FORMER_NAME;

    @Schema(name = "KANJI_WRITING", description = "汉字书写")
    @JsonProperty("KANJI_WRITING")private String KANJI_WRITING;

    @Schema(name = "MINORITY_WRITING", description = "少数民族语书写")
    @JsonProperty("MINORITY_WRITING")private String MINORITY_WRITING;

    @Schema(name = "ORIGINAL_PRONUNCIATION", description = "地名原读音")
    @JsonProperty("ORIGINAL_PRONUNCIATION")private String ORIGINAL_PRONUNCIATION;

    @Schema(name = "MANDARIN_PRONUNCIATION", description = "汉语普通话读音")
    @JsonProperty("MANDARIN_PRONUNCIATION")private String MANDARIN_PRONUNCIATION;

    @Schema(name = "ROMAN_ALPHABET_SPELLING", description = "罗马字母拼写")
    @JsonProperty("ROMAN_ALPHABET_SPELLING")private String ROMAN_ALPHABET_SPELLING;

    @Schema(name = "TOPONYMIC_LANGUAGE", description = "地名语种")
    @JsonProperty("TOPONYMIC_LANGUAGE")private String TOPONYMIC_LANGUAGE;

    @Schema(name = "TOPONYMIC_CATEGORY", description = "地名类别")
    @JsonProperty("TOPONYMIC_CATEGORY")private String TOPONYMIC_CATEGORY;

    @Schema(name = "EAST_LONGITUDE", description = "东经")
    @JsonProperty("EAST_LONGITUDE")private String EAST_LONGITUDE;

    @Schema(name = "TO_EAST_LONGITUDE", description = "至东经")
    @JsonProperty("TO_EAST_LONGITUDE")private String TO_EAST_LONGITUDE;

    @Schema(name = "NORTH_LATITUDE", description = "北纬")
    @JsonProperty("NORTH_LATITUDE")private String NORTH_LATITUDE;

    @Schema(name = "TO_NORTHERN_LATITUDE", description = "至北纬")
    @JsonProperty("TO_NORTHERN_LATITUDE")private String TO_NORTHERN_LATITUDE;

    @Schema(name = "CENSUS_STATUS", description = "地名普查状态")
    @JsonProperty("CENSUS_STATUS")private String CENSUS_STATUS;

    @Schema(name = "ORIGINAL_IMAGE_NAME", description = "原图名称")
    @JsonProperty("ORIGINAL_IMAGE_NAME")private String ORIGINAL_IMAGE_NAME;

    @Schema(name = "FIGURE_NUMBER", description = "图号（年版）")
    @JsonProperty("FIGURE_NUMBER")private String FIGURE_NUMBER;

    @Schema(name = "SCALE", description = "比例尺")
    @JsonProperty("SCALE")private String SCALE;

    @Schema(name = "TOPONYM_ORIGIN", description = "地名的来历")
    @JsonProperty("TOPONYM_ORIGIN")private String TOPONYM_ORIGIN;

    @Schema(name = "TOPONYM_MEANING", description = "地名的含义")
    @JsonProperty("TOPONYM_MEANING")private String TOPONYM_MEANING;

    @Schema(name = "TOPONYM_HISTORY", description = "地名的历史沿革")
    @JsonProperty("TOPONYM_HISTORY")private String TOPONYM_HISTORY;

    @Schema(name = "SECRET_LEVEL", description = "密级")
    @JsonProperty("SECRET_LEVEL")private String SECRET_LEVEL;

    @Schema(name = "COORDINATE_SYSTEM", description = "坐标系")
    @JsonProperty("COORDINATE_SYSTEM")private String COORDINATE_SYSTEM;

    @Schema(name = "MEASUREMENT_METHOD", description = "测量方法")
    @JsonProperty("MEASUREMENT_METHOD")private String MEASUREMENT_METHOD;

    @Schema(name = "ENTITY_OVERVIEW", description = "地理实体概况")
    @JsonProperty("ENTITY_OVERVIEW")private String ENTITY_OVERVIEW;

    @Schema(name = "MULTIMEDIA_INFORMATION", description = "多媒体信息")
    @JsonProperty("MULTIMEDIA_INFORMATION")private String MULTIMEDIA_INFORMATION;

    @Schema(name = "SOURCE", description = "资料来源")
    @JsonProperty("SOURCE")private String SOURCE;

    @Schema(name = "REMARK", description = "备注")
    @JsonProperty("REMARK")private String REMARK;

    @Schema(name = "REGISTRATION_TIME", description = "登记时间")
    @JsonProperty("REGISTRATION_TIME")private String REGISTRATION_TIME;

    @Schema(name = " REGISTRANT", description = "登记人")
    @JsonProperty(" REGISTRANT")private String REGISTRANT;

    @Schema(name = "REGISTRATION_UNIT", description = "登记单位")
    @JsonProperty("REGISTRATION_UNIT")private String REGISTRATION_UNIT;

    @Schema(name = "COMMON_ROMAN_SPELLING", description = "通名罗马字母拼写")
    @JsonProperty("COMMON_ROMAN_SPELLING")private String COMMON_ROMAN_SPELLING;

    @Schema(name = "ESTABLISHMENT_YEAR", description = "设立年份")
    @JsonProperty("ESTABLISHMENT_YEAR")private String ESTABLISHMENT_YEAR;

    @Schema(name = "REPEAL_YEAR", description = "废止年份")
    @JsonProperty("REPEAL_YEAR")private String REPEAL_YEAR;

    @Schema(name = "ADMINISTRATIVE_DISTRICT", description = "所跨行政区")
    @JsonProperty("ADMINISTRATIVE_DISTRICT")private String ADMINISTRATIVE_DISTRICT;

    @Schema(name = "ADDITIONAL_INFORMATION", description = "其他信息")
    @JsonProperty("ADDITIONAL_INFORMATION")private String ADDITIONAL_INFORMATION;

    @Schema(name = "SPECIAL_INFORMATION", description = "特殊信息")
    @JsonProperty("SPECIAL_INFORMATION")private Object SPECIAL_INFORMATION;

    @Schema(name = "CATEGORY_NAME", description = "地名分类")
    @JsonProperty("CATEGORY_NAME")private Object CATEGORY_NAME;

    @Schema(name = "FTS_CHAR", description = "单字分词")
    @JsonProperty("FTS_CHAR")private String FTS_CHAR;

    @Schema(name = "FTS_WORD", description = "词语分词")
    @JsonProperty(" FTS_WORD")private String FTS_WORD;

}
