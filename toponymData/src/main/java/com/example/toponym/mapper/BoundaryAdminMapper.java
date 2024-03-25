package com.example.toponym.mapper;
import com.example.toponym.model.BoundaryAdmin;
import java.util.List;
import java.util.Map;
public interface BoundaryAdminMapper {
    List<BoundaryAdmin> findAll();
    List<Map<String,Object>> findByAdmin(String admin);
}
