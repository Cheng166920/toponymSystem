package com.example.toponym.mapper;

import com.example.toponym.model.Action;
import com.example.toponym.model.BoundaryRequest;
import io.swagger.v3.oas.annotations.Parameter;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BoundaryMapper {
    JSONObject findBoundary(String level);
    JSONObject findBoundaryStake();
    JSONObject findBoundaryPoint();
    JSONObject findTripleNode();
    Map<String,Object> getBoundary(String IDENTIFICATION_CODE);
    String[] findField(String table);
    List<Map<String,Object>> findInfo(String[] params,String type);
    List<Map<String,Object>> findByName(String[] params,String boundaryName);
    String getBoundaryType(String boundaryName);
    Map<String,Object> getFile(String code, String fileType,String table);
    String getTableName(String code);
    Map<String,Object> findByCode(String[] params,String code);
    List<Map<String,Object>> DownloadData(Integer gid);
    void updateReview(String review,Integer gid);
    void updateRequest(String action_status,Integer gid);
    void deleteRequest(Integer gid);
    void addRequest(Action action);
    Map<String,Object> findByGID(Integer gid);
    List<Map<String,Object>> findAll();
}
