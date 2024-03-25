package com.example.toponym.mapper;
import com.example.toponym.model.BoundaryCategory;
import java.util.List;
import java.util.Map;

public interface BoundaryCategoryMapper {
    List<BoundaryCategory> findAll();
    List<Map<String,Object>> findSUM();
    List<Map<String,Object>> findByRank(String rank);
}
