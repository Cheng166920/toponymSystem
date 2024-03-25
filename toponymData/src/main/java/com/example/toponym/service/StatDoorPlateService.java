package com.example.toponym.service;
import com.example.toponym.model.StatDoorPlate;
import java.util.List;
import java.util.Map;

public interface StatDoorPlateService {
    List<StatDoorPlate> findAll();
    List<Map<String,Object>> findSUM();
    List<Map<String,Object>> findByRoad(String road);
    List<Map<String,Object>> findRoad();
    List<Map<String,Object>> findTown();
    List<Map<String,Object>> findByAdd(String start,String end);
    List<Map<String,Object>> findByUpdate(String start,String end);
}
