package com.example.toponym.service.impl;
import com.example.toponym.mapper.BoundaryAdminMapper;
import com.example.toponym.model.BoundaryAdmin;
import com.example.toponym.service.BoundaryAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoundaryAdminImpl implements BoundaryAdminService{
    @Autowired
    private BoundaryAdminMapper boundaryAdminMapper;

    @Override
    public List<BoundaryAdmin> findAll(){
        return boundaryAdminMapper.findAll();}

    @Override
    public List<Map<String,Object>> findByAdmin(String admin){
        return boundaryAdminMapper.findByAdmin(admin);}
}
