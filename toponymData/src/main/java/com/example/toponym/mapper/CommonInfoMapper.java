package com.example.toponym.mapper;

import com.example.toponym.model.CommonInfo;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CommonInfoMapper {
    int selectCount();
    List<Map<String, Object>> selectFtsChar(String[] params, String query);
    List<Map<String, Object>> selectFtsWord(String[] params, String query);
    List<CommonInfo> findByJson(CommonInfo commonInfo);
    String[] findPublicField();
    List<String> getWord();
    List<String> getCategory(String level);
    List<String> getNextCategory(String nextLevel,String level,String category);
    List<Map<String,Object>> getSpecialField(String category);
    List<Map<String, Object>> findInfo(@Param("array") String[] params, @Param("commonInfo") CommonInfo commonInfo);
    void addCommonInfo(CommonInfo commonInfo);
    void deleteCommonInfo(String IDENTIFICATION_CODE);
    void updateCommonInfo(CommonInfo commonInfo);
    List<CommonInfo> findByIDENTIFICATION_CODE(String IDENTIFICATION_CODE);
    CommonInfo findByCode(String IDENTIFICATION_CODE);
    String getFieldExplain(String field,String category);
    List<CommonInfo> selectAll();
}
