package com.example.toponym.mapper;

import com.example.toponym.model.Action;
import com.example.toponym.model.ToponymHistory;

import java.util.List;
import java.util.Map;

public interface ActionMapper {
    Boolean isValid(String input_code);
    void addAction(Action action);
    void updateReview(Action action);
    void updateAction(Action action);
    void deleteAction(Integer gid);
    void addHistory(ToponymHistory history);
    List<Map<String,Object>> findAll();
    Map<String,Object> findByGID(Integer gid);
    String getCategoryName(String level, String code);
    Map<String,String> getCategoryCode(String category_name);
    String getToponymCode(String township);
    List<String> getCodeList(String toponym_code);
    String getTownshipCode(String pid, String name);
    Boolean codeIsValid(String category_code);

}
