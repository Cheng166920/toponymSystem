package com.example.toponym.service;

import com.example.toponym.model.PointExtent;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ExtentService {

    JSONObject findPointByExtent(String polygon,Integer scale);
    JSONObject findLineByExtent(String polygon,Integer scale);
    com.alibaba.fastjson.JSON findDoorplateByExtent(String polygon);
    JSONObject findSign();
    //List<String> findCategory(Integer scale);
}
