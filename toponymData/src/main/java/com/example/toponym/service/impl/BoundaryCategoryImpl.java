package com.example.toponym.service.impl;
import com.example.toponym.mapper.BoundaryCategoryMapper;
import com.example.toponym.model.BoundaryCategory;
import com.example.toponym.service.BoundaryCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoundaryCategoryImpl implements BoundaryCategoryService {
    @Autowired
    private BoundaryCategoryMapper boundaryMapper;

    @Override
    public List<BoundaryCategory> findAll(){
        return boundaryMapper.findAll();}
    @Override
    public List<Map<String,Object>> findSUM(){
        return boundaryMapper.findSUM();}
    @Override
    public List<Map<String,Object>> findByRank(String rank){
        return boundaryMapper.findByRank(rank);}
}
