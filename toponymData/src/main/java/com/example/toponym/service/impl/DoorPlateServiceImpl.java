package com.example.toponym.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.toponym.mapper.DoorPlateMapper;
import com.example.toponym.model.*;
import com.example.toponym.service.DoorPlateService;
import com.example.toponym.utils.PdfUtils;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.model.crf.CRFSegmenter;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

@Service
public class DoorPlateServiceImpl implements DoorPlateService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DoorPlateMapper doorPlateMapper;

    //模糊检索
    @Override
    public List<DoorPlateElement> selectFts(String query, int currentPage, int pageSize) {
        List<DoorPlateElement> ftsResult = null;
        PageHelper.startPage(currentPage,pageSize);
        ftsResult = doorPlateMapper.selectWord(query);
        if(ftsResult.isEmpty()){
        try {
            PageHelper.startPage(currentPage,pageSize);
            ftsResult = doorPlateMapper.selectFtsWord(StringToWord(query));
        } catch (IOException e) {
            e.printStackTrace();
        }}
        assert ftsResult != null;
        if(ftsResult.isEmpty()){
            PageHelper.startPage(currentPage,pageSize);
            ftsResult = doorPlateMapper.selectFtsChar(StringToCharList(query));
        }
        return ftsResult;
    }
    //分类检索
    @Override
    public List<DoorPlateElement> catQuery(String field, String value){
        return doorPlateMapper.catQuery(field,value);
    }

    //依据id检索
    @Override
    public List<DoorPlateElement> findById(String id){
        return doorPlateMapper.findById(id);
    }

    //查询字段下的所有值
    @Override
    public JSONObject getFiledValue(String field){
        JSONObject json = new JSONObject();
        json.put("field_name",field);
        if(field.equals("geom")||field.equals("geometry")){
            field = "ST_AsGeoJson(geometry) as geom";
        }
        json.put("value",doorPlateMapper.getFiledValue(field));
        return json;
    }

    //地址分词
    @Override
    public String segment(String query){
        CRFLexicalAnalyzer segment = null;
        try {
            segment = new CRFLexicalAnalyzer(HanLP.Config.CRFCWSModelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert segment != null;
        segment.enablePartOfSpeechTagging(true);
        return segment.seg(query).toString();
    }

    //添加数据
    @Override
    public String addDoorplate(DoorPlateElement doorPlateElement) {
        if(doorPlateMapper.isInsertValid(doorPlateElement) == null){
            createUID(doorPlateElement);
            doorPlateMapper.addSourceDoorplate(doorPlateElement);
            doorPlateMapper.addAppDoorplate(toFtsFormat(doorPlateElement));
            return "0";
        }
        return doorPlateMapper.isInsertValid(doorPlateElement); // 数据无效，返回uid
    }

    //删除数据
    @Override
    public int deleteDoorplate(String uid){
        return doorPlateMapper.deleteDoorplate(uid);
    }

    //更新数据
    @Override
    public int updateDoorplate(DoorPlateElement doorPlateElement){
        if(doorPlateMapper.isValid(doorPlateElement.getUid())){
            //数据有效，返回0
            doorPlateMapper.updateSourceDoorplate(doorPlateElement);
            DoorPlateElement updateResult = doorPlateMapper.findById(doorPlateElement.getUid()).get(0);
            doorPlateMapper.updateAppDoorplate(toFtsFormat(updateResult));
            return 0;
        }
        return -1; // 数据无效，返回-1
    }

    //提交审核数据
    @Override
    public int addAction(Action action){
        JSONObject input_content = JSONObject.fromObject(action.getCONTENT());
        String input_action = action.getACTION();
        Boolean validity;
        if (input_action.equals("业务新增") || input_action.equals("公众申报")){
            DoorPlateElement doorPlateElement = new Gson().fromJson(action.getCONTENT().toString(), DoorPlateElement.class);
            String code = doorPlateMapper.isInsertValid(doorPlateElement);
            validity = code == null;
        }else{
            validity = doorPlateMapper.isValid(input_content.getString("uid"));
        }
        if(validity){
            action.setCONTENT(input_content);
            doorPlateMapper.addAction(action);
            return 0;
        }
        return -1;
    }


    //更新审核状态
    @Override
    public void updateReview(String review,int gid){
        //业务逻辑同步更新
        if(review.equals("审核通过")){
            Action updateMessage = doorPlateMapper.findByGID(gid);
            DoorPlateElement doorPlateElement = new Gson().fromJson(updateMessage.getCONTENT().toString(), DoorPlateElement.class);
            String action = updateMessage.getACTION();
            switch (action) {
                case "公众申报", "业务新增" -> addDoorplate(doorPlateElement);
                case "公众纠错", "业务修改" -> updateDoorplate(doorPlateElement);
                case "业务删除" -> deleteDoorplate(doorPlateElement.getUid());
            }
        }
        doorPlateMapper.updateReview(review,gid);
    }
    //更新生产表属性
    @Override
    public int updateAction(Action action){
        String input_content = new Gson().toJson(action.getCONTENT());
        String input_action = action.getACTION();
        int gid = action.getGID();
        Boolean validity;
        if (input_action.equals("业务新增") || input_action.equals("公众申报")){
            DoorPlateElement doorPlateElement = new Gson().fromJson(input_content, DoorPlateElement.class);
            String code = doorPlateMapper.isInsertValid(doorPlateElement);
            validity = code == null;
        }else{
            validity = doorPlateMapper.isValid(JSONObject.fromObject(input_content).getString("uid"));
        }
        if(validity && doorPlateMapper.findByGID(gid) != null){
            action.setCONTENT(input_content);
            doorPlateMapper.updateAction(action);
            return 0;
        }
        return -1;
        }

    //删除生产表数据
    @Override
    public void deleteAction(Integer gid){doorPlateMapper.deleteAction(gid);}

    //查询审核数据
    @Override
    public List<Action> findAll(){
        List<Action> list = doorPlateMapper.findAll();
        for (Action action : list) {
            com.alibaba.fastjson.JSON obj = com.alibaba.fastjson.JSON.parseObject(String.valueOf(action.getCONTENT()));
            action.setCONTENT(obj);
        }
        return list;
    }

    //查询单条审核数据
    @Override
    public Action findByGID(long gid){
        Action action = doorPlateMapper.findByGID(gid);
        com.alibaba.fastjson.JSON input_content = com.alibaba.fastjson.JSON.parseObject(String.valueOf(action.getCONTENT()));
        action.setCONTENT(input_content);
        return action;
    }

    @Override
    public Map<String,Object> findUpdateByGID(long gid){
        Action action = doorPlateMapper.findByGID(gid);
        com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(action.getCONTENT()));
        action.setCONTENT(obj);
        String action_type = action.getACTION();
        Map<String,Object> update_action = new HashMap<>();
        Field[] fields = action.getClass().getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
            try {
                update_action.put(new String(field.getName()),field.get(action));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (action_type.equals("公众纠错")||action_type.equals("业务修改")) {
            com.alibaba.fastjson.JSONObject new_content = new com.alibaba.fastjson.JSONObject();
            new_content.putAll(obj);
            String id = new_content.getString("uid");
            DoorPlateElement doorp = doorPlateMapper.findById(id).get(0);
            Map<String, Object> old_content = JSON.parseObject(JSON.toJSONString(doorp), Map.class);
            try {
                new_content.remove("otherMessage");
            } catch (Exception e) {
                e.printStackTrace();
            }
            com.alibaba.fastjson.JSONArray update_field = new com.alibaba.fastjson.JSONArray();
            for(Map.Entry<String, Object> entry : new_content.entrySet()){
                String key = entry.getKey();
                String new_value = new_content.getString(key);
                String old_value = old_content.get(key).toString();
                if (!new_value.equals(old_value)) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("field_name", key);
                    map.put("new_value", new_value);
                    map.put("old_value", old_value);
                    update_field.add(map);
                }
            }
            update_action.put("UPDATE_FIELD",update_field);
        }
        return update_action;
    }

    //获取单条地址历史变更信息
    @Override
    public List<DoorPlateHistory> findHistoryByUID(String uid){
        List<DoorPlateHistory> historyList = doorPlateMapper.findHistoryByUID(uid);
        for (DoorPlateHistory list : historyList) {
            JSONArray obj = JSONArray.fromObject(list.getChange_jsonb());
            list.setChange_jsonb(obj);
        }
        return historyList;
    }

    //获取单条地址历史变更详细信息
    @Override
    public List<DoorPlateRecord> findHistoryDetail(String uid,String change_time){
        List<DoorPlateRecord> doorPlateRecordList =  doorPlateMapper.findHistoryDetail(uid,change_time);
        for(DoorPlateRecord list : doorPlateRecordList){
            com.alibaba.fastjson.JSON new_value = com.alibaba.fastjson.JSON.parseObject(String.valueOf(list.getNew_value()));
            com.alibaba.fastjson.JSON old_value = com.alibaba.fastjson.JSON.parseObject(String.valueOf(list.getOld_value()));
            list.setNew_value(new_value);
            list.setOld_value(old_value);
        }
        return doorPlateRecordList;
    }

    //获取门牌登记表
    @Override
    public void getRegisterTable(Map<String,String> map, HttpServletResponse response){

        String templatePath = "/static/register/doorplate_register.pdf";
        try {
            String fileName = map.get("uid").toString() + ".pdf";
            response.reset();
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName,"UTF-8"));
            OutputStream out = response.getOutputStream();

            PdfUtils.pdfout(templatePath,map,out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取行政区划
    @Override
    public List<Map<String,Object>> getCityTree(){
        List<Map<String,String>> province = doorPlateMapper.selectCity("0");
        List<Map<String,Object>> tree = new ArrayList<>();
        String code = null;
        for (Map<String,String> str:province) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("label",str.get("name"));
            code = str.get("code");
            List<Map<String,String>> city = doorPlateMapper.selectCity(code);
            JSONArray province_child =  new JSONArray();
            for (Map<String,String> city_str:city) {
                Map<String,Object> city_map = new LinkedHashMap<>();
                city_map.put("label",city_str.get("name"));
                if(city_str.get("name") == "市辖区")
                    continue;
                code = city_str.get("code");
                List<Map<String,String>> district = doorPlateMapper.selectCity(code);
                JSONArray city_child =  new JSONArray();
                for (Map<String,String> district_str:district) {
                    Map<String, Object> district_map = new LinkedHashMap<>();
                    district_map.put("label", district_str.get("name"));
                    code =district_str.get("code");
                    List<Map<String,String>> township = doorPlateMapper.selectCity(code);
                    JSONArray district_child = new JSONArray();
                    for (Map<String,String> township_str:township) {
                        Map<String, Object> township_map = new LinkedHashMap<>();
                        township_map.put("label", township_str.get("name"));
                        code = township_str.get("code");
                        List<Map<String,String>> village = doorPlateMapper.selectCity(code);
                        JSONArray township_child = new JSONArray();
                        for (Map<String,String> village_str:village) {
                            Map<String, Object> village_map = new LinkedHashMap<>();
                            village_map.put("label", village_str.get("name"));
                            township_child.add(village_map);
                        }
                        township_map.put("children",township_child);
                        district_child.add(township_map);
                    }
                    district_map.put("children",district_child);
                    city_child.add(district_map);
                }
                city_map.put("children",city_child);
                province_child.add(city_map);
            }
            map.put("children",province_child);
            tree.add(map);
        }
        return tree;
    }

    @Override
    public List<Map<String,String>> getDistrict(String pid){
        return doorPlateMapper.selectCity(pid);

    }
    @Override
    public String getCityCode(DoorPlateElement doorPlateElement){
        return doorPlateMapper.selectCityCode(doorPlateElement);
    }




    private static DoorPlateAElement toFtsFormat(DoorPlateElement doorPlateElement){
        StringBuilder builder = new StringBuilder();
        builder.append(doorPlateElement.getProvince()).append(doorPlateElement.getCity()).append(doorPlateElement.getDistrict())
                .append(doorPlateElement.getTownship()).append(doorPlateElement.getVillage()).append(doorPlateElement.getRoad())
                .append(doorPlateElement.getRoad_direction())
                .append(doorPlateElement.getEstate()).append(doorPlateElement.getBuilding()).append(doorPlateElement.getUnit())
                .append(doorPlateElement.getDoorplate()).append(doorPlateElement.getDoorplate_sub()).append(doorPlateElement.getRoom())
                .append(doorPlateElement.getCategory()).append(doorPlateElement.getConstruction());

        String text = builder.toString().replaceAll("null", "");
        DoorPlateAElement doorPlateAElement = new DoorPlateAElement();
        doorPlateAElement.setUid(doorPlateElement.getUid());
//        BeanUtils.copyProperties(doorPlateElement,doorPlateAElement);
        doorPlateAElement.setFts_char(StringToCharList(text).replaceAll("&"," "));
        try {
            doorPlateAElement.setFts_word(StringToWord(text).replaceAll("&"," "));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doorPlateAElement;
    }


    private static String StringToCharList(String query) {
        StringBuilder charList = new StringBuilder();
        if(query == null){
            return "";
        }else {
            char[] letters = query.toCharArray();
            for (char letter : letters) {
                if (Character.isDigit(letter) || letter == ' ' || letter == '-' || letter == '.' || (letter >= 'A' && letter <= 'Z') || (letter >= 'a' && letter <= 'z')) {
                    charList.append(letter);
                } else {
                    charList.append("&");
                    charList.append(letter);
                    charList.append("&");
                }
            }
            if(charList.charAt(0)=='&')
                charList.deleteCharAt(0);
            if(charList.charAt(charList.length()-1)=='&')
                charList.deleteCharAt(charList.length()-1);

            return charList.toString().replaceAll("&&","&");
        }
    }

    private static String StringToWord(String query) throws IOException {
        CRFSegmenter segment = new CRFSegmenter();
        return segment.segment(query).toString().replace(", ","&")
                .replace("[","").replace("]","");
    }

    private DoorPlateElement createUID(DoorPlateElement doorPlateElement){
        Long uid = doorPlateMapper.selectMaxUID(doorPlateElement);
        if(uid == null){
            String code = doorPlateMapper.selectCityCode(doorPlateElement);
            doorPlateElement.setUid(code + "1400001");
        }else{
            uid = uid + 1;
            doorPlateElement.setUid(String.valueOf(uid));
        }
        return doorPlateElement;
    }



}
