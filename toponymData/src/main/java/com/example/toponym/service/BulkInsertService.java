package com.example.toponym.service;

import com.example.toponym.model.DoorPlateAElement;
import com.example.toponym.model.DoorPlateElement;

import java.util.List;
import java.util.Map;

public interface BulkInsertService {
    void deleteData();
    List<DoorPlateElement> findAllData();
    List<DoorPlateAElement> getApplicationData();
    void writeData(String filePath);
    void writeApplicationData(String filePath);
    void insertSourceTable();
    void insertApplicationTable();

    List<Long> selectMaxUID(List<Map<String,Object>> list);

}
