package com.example.toponym.service;
import com.example.toponym.model.BoundaryAdmin;
import java.util.List;
import java.util.Map;
public interface BoundaryAdminService {
    List<BoundaryAdmin> findAll();
    List<Map<String,Object>> findByAdmin(String admin);
}
