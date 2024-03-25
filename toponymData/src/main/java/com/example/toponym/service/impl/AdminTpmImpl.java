package com.example.toponym.service.impl;

import com.example.toponym.mapper.AdminTpmMapper;
import com.example.toponym.model.AdminTpm;
import com.example.toponym.model.Category;
import com.example.toponym.service.AdminTpmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminTpmImpl implements AdminTpmService{
    @Autowired
    private AdminTpmMapper adminTpmMapper;

    @Override
    public List<AdminTpm> findAll(){
        return adminTpmMapper.findAll();}
    @Override
    public List<Map<String,Object>> findSUM(){
        return adminTpmMapper.findSUM();}
   @Override
    public List<Map<String,Object>> findByName(String category){
        return adminTpmMapper.findByName(category);}
    @Override
   public List<Map<String,Object>> findByRank(String admin){
        return adminTpmMapper.findByRank(admin);}

    @Override
    public List<Map<String,Object>> findByPop(){
        return adminTpmMapper.findByPop();}
    @Override
    public List<Map<String,Object>> findByCategory(){
        return adminTpmMapper.findByCategory();}
    @Override
    public List<Map<String,Object>> findByAdd(String start,String end){
        return adminTpmMapper.findByAdd(start, end);}
    @Override
    public List<Map<String,Object>> findByUpdate(String start,String end){
        return adminTpmMapper.findByUpdate(start, end);}

}
