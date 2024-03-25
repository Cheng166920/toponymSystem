package com.example.toponym.mapper;

import com.example.toponym.model.*;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper

public interface DoorPlateMapper {
    //门牌检索
    List<DoorPlateElement> selectFtsChar(String query);
    List<DoorPlateElement> selectFtsWord(String query);
    List<DoorPlateElement> selectWord(String query);
    List<DoorPlateElement> catQuery(String field,String value);
    List<String> getFiledValue(String field);
    List<DoorPlateElement> findById(String id);

    //门牌增删改操作
    Boolean isValid(String uid);
    String isInsertValid(DoorPlateElement doorPlateElement);
    int addAppDoorplate(DoorPlateAElement doorPlateAElement);
    int addSourceDoorplate(DoorPlateElement doorPlateElement);
    int deleteDoorplate(String uid);
    void updateSourceDoorplate(DoorPlateElement doorPlateElement);
    void updateAppDoorplate(DoorPlateAElement doorPlateAElement);

    //门牌审核/查询
    int addAction(Action action);
    void updateReview(String review, int gid);
    void updateAction(Action action);
    List<Action> findAll();
    Action findByGID(long gid);
    void deleteAction(Integer gid);

    //门牌历史查询
    List<DoorPlateRecord> findHistoryDetail(String uid,String change_time);
    List<DoorPlateHistory> findHistoryByUID(String uid);

    //辅助查询
    Long selectMaxUID(DoorPlateElement doorPlateElement);
    String selectAdministration(DoorPlateElement doorPlateElement);
    String selectCityCode(DoorPlateElement doorPlateElement);
    List<Map<String,String>> selectCity(String pid);

}
