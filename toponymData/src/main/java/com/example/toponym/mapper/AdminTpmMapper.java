package com.example.toponym.mapper;

import com.example.toponym.model.AdminTpm;
import com.example.toponym.model.Category;
import java.util.List;
import java.util.Map;

public interface AdminTpmMapper {
    List<AdminTpm> findAll();
    List<Map<String,Object>> findSUM();
    List<Map<String,Object>> findByName(String category);
    List<Map<String,Object>> findByRank(String admin);
    List<Map<String,Object>> findByPop();
    List<Map<String,Object>> findByCategory();
    List<Map<String,Object>> findByAdd(String start,String end);
    List<Map<String,Object>> findByUpdate(String start,String end);
}
