package com.example.toponym.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(name = "Boundary对象", description = "勘界应用数据库对象")
public class Boundary implements Serializable {
    private Integer GID;
    //@Schema(name = "IDENTIFICATION_CODE", description = "标识码")
    private String IDENTIFICATION_CODE;
    private String TYPE;
    private String LEVEL;
    private Object GEOM;
    private String BOUNDARY_CODE;
    private String BOUNDARY_STAKE_NUMBER;
    private String BOUNDARY_POINT;
    private String TRIPLE_INTERSECTION_POINT;
    private BigDecimal LONGITUDE;
    private BigDecimal LATITUDE;
    private String RANK;
    private String LOCATION_DESCRIPTION;
    private BigDecimal ABSCISSA;
    private BigDecimal ORDINATE;
    private String BOUNDARY_NAME;
    private String BOUNDARY_RANK;
    private String BOUNDARY_LENGTH;
    private String DISPUTE_OR_NOT;
    private BigDecimal AREA;
    private String TOWN_ADMIN;
    private String COUNT_ADMIN;
    private String TOWN_CODE;
    private String GRAPH;
    private String BOUNDARY_POINT_TABLE;
    private String AGREEMENT;
    private String AGREEMENT_FIGURE;
}