package com.example.toponym.service;

import com.example.toponym.model.PageBean;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface BoundaryService {
    JSONObject findBoundary(String level);
    JSONObject findBoundaryStake();
    JSONObject findBoundaryPoint();
    JSONObject findTripleNode();
    Map<String,Object> getBoundary(String IDENTIFICATION_CODE);
    Map<String,Object> downloadRequest(String code,String user);
    List<Map<String,Object>> findAllRequest();
    void downloadShp(Integer gid,HttpServletResponse response);
    Map<String,Object> downloadReview(Integer gid, String review);
    List<Map<String,Object>> findInfo(String type);
    PageBean<Map<String,Object>> findByName(String name, Integer currentPage, Integer pageSize);
    byte[] getFile(String code, String fileType,HttpServletResponse response);
    Map<String,Object> findByCode(String code);
    void deleteRequest(Integer gid);
    Map<String,Object> findByGID(Integer gid);
}
