package com.example.toponym.service.impl;
import com.example.toponym.mapper.BoundarySideMapper;
import com.example.toponym.model.BoundarySide;
import com.example.toponym.service.BoundarySideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoundarySideImpl implements BoundarySideService{
    @Autowired
    private BoundarySideMapper boundarySideMapper;

    @Override
    public List<BoundarySide> findAll(){
        return boundarySideMapper.findAll();}
}
