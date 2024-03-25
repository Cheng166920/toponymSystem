package com.example.toponym.mapper;

import com.example.toponym.model.Subject;

import java.util.List;
import java.util.Map;

public interface SubjectMapper {
    List<Map<String,Object>> findAll();
    List<Map<String,Object>> findBySUB(String subname);
}
