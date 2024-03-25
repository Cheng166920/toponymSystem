package com.example.toponym.service;

import com.example.toponym.model.Subject;

import java.util.List;
import java.util.Map;

public interface SubjectService {
    //返回所有地名类别统计
    List<Map<String,Object>> findAll();
    //某一地名类别查询
    List<Map<String,Object>> findBySUB(String subname);
}
