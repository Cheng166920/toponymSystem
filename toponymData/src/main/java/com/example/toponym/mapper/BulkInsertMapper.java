package com.example.toponym.mapper;

import com.example.toponym.model.DoorPlateAElement;
import com.example.toponym.model.DoorPlateElement;

import java.util.List;
import java.util.Map;

public interface BulkInsertMapper {
    void deleteData();
    List<DoorPlateElement> findAllData();
    void insertSourceTable();
    void insertApplicationTable();
    List<Long> selectMaxUID(List<Map<String,Object>> list);
}
