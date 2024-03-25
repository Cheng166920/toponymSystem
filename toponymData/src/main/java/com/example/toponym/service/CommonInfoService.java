package com.example.toponym.service;

import com.example.toponym.model.CommonInfo;
import com.example.toponym.model.PageBean;
import com.example.toponym.model.page.CommonInfoPage;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonInfoService {
    //int selectCount();
    List<Map<String, Object>> ftsQuery(String query,Integer currentPage,Integer pageSize);
    List<CommonInfo> findByJson(CommonInfo commonInfo);
    //String[] findPublicField();
    List<String> getWord();
    List<Map<String,Object>> getCategoryTree();
    List<Map<String,Object>> getSpecialField(String category);
    List<Map<String, Object>> findInfo(@Param("commonInfo") CommonInfo commonInfo);
    PageBean<Map<String, Object>> findInfoPage(CommonInfoPage commonInfoPage);
    void addCommonInfo(CommonInfo commonInfo);
    void deleteCommonInfo(String IDENTIFICATION_CODE);
    void updateCommonInfo(CommonInfo commonInfo);
    Map<String, String> findByIDENTIFICATION_CODE(String IDENTIFICATION_CODE);
    byte[] getRegisterTable(Map<String,String> map, HttpServletResponse response);
    List<CommonInfo> selectAll();
}
