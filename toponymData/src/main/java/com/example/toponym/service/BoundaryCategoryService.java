package com.example.toponym.service;
import com.example.toponym.model.BoundaryCategory;
import java.util.List;
import java.util.Map;
public interface BoundaryCategoryService {
    List<BoundaryCategory> findAll();
    List<Map<String,Object>> findSUM();
    List<Map<String,Object>> findByRank(String rank);
}
