package com.example.toponym.mapper;

import com.example.toponym.model.PointExtent;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ExtentMapper {

    JSONObject findPointByExtent(String polygon);
    JSONObject findLineByExtent(String polygon);
    JSONObject findDoorplateByExtent(String polygon);
    List<String> findCategory(Integer scale);
    JSONObject findSign();
}
