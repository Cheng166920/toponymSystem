package com.example.toponym.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.toponym.mapper.SubjectMapper;
import com.example.toponym.model.Subject;
import com.example.toponym.service.SubjectService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SubjectImpl implements SubjectService{
    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public List<Map<String,Object>> findAll(){

        return subjectMapper.findAll();}
    @Override
    public List<Map<String,Object>> findBySUB(String subname){
        //Map<String,Object> map = subjectMapper.findBySUB(subname).get(0);
        return subjectMapper.findBySUB(subname);}
}
