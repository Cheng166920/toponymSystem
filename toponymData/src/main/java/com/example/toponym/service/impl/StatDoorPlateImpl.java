package com.example.toponym.service.impl;
import com.example.toponym.mapper.StatDoorPlateMapper;
import com.example.toponym.model.StatDoorPlate;
import com.example.toponym.service.StatDoorPlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatDoorPlateImpl implements StatDoorPlateService{
    @Autowired
    private StatDoorPlateMapper statDoorPlateMapper;

    @Override
    public List<StatDoorPlate> findAll(){
        return statDoorPlateMapper.findAll();}
    @Override
    public List<Map<String,Object>> findSUM(){
        return statDoorPlateMapper.findSUM();}
    @Override
    public List<Map<String,Object>> findByRoad(String road){
        return statDoorPlateMapper.findByRoad(road);}
    @Override
    public List<Map<String,Object>> findRoad(){
        return statDoorPlateMapper.findRoad();}
    @Override
    public List<Map<String,Object>> findTown(){
        return statDoorPlateMapper.findTown();}
    @Override
    public List<Map<String,Object>> findByAdd(String start,String end){
        return statDoorPlateMapper.findByAdd(start,end);}
    @Override
    public List<Map<String,Object>> findByUpdate(String start,String end){
        return statDoorPlateMapper.findByUpdate(start,end);}
}
