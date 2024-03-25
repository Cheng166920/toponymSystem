package com.example.toponym.service;

import com.example.toponym.model.*;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DoorPlateService {
    //门牌检索
    List<DoorPlateElement> selectFts(String query,int currentPage,int pageSiz);
    String segment(String query);
    List<DoorPlateElement> catQuery(String field,String value);
    JSONObject getFiledValue(String field);
    List<DoorPlateElement> findById(String id);

    //门牌增删改操作
    String addDoorplate(DoorPlateElement doorPlateElement);
    int deleteDoorplate(String uid);
    int updateDoorplate(DoorPlateElement doorPlateElement);

    //门牌审核/查询
    int addAction(Action action);
    void updateReview(String review,int gid);
    int updateAction(Action action);
    List<Action> findAll();
    Action findByGID(long gid);
    Map<String,Object> findUpdateByGID(long gid);
    void deleteAction(Integer gid);

    //门牌历史查询
    List<DoorPlateRecord> findHistoryDetail(String uid,String change_time);
    List<DoorPlateHistory> findHistoryByUID(String uid);

    //门牌登记表
    void getRegisterTable(Map<String,String> map, HttpServletResponse response);

    //行政区划
    List<Map<String,Object>> getCityTree();
    List<Map<String,String>> getDistrict(String pid);
    String getCityCode(DoorPlateElement doorPlateElement);

}
