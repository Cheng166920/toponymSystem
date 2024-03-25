package com.example.toponym.service;

import com.example.toponym.model.Action;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface ActionService {
    Boolean isValid(Action action);
    Map<String,Object> addAction(Action action);
    Map<String, Object> updateReview(Action action);
    void updateAction(Action action);
    void deleteAction(Integer gid);
    List<Map<String,Object>> findAll();
    Map<String,Object> findByGID(Integer gid);
    Map<String,Object> findUpdateByGID(Integer gid);
    String setIdentificationCode();
}
