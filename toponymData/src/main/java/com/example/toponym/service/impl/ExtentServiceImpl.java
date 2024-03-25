package com.example.toponym.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.toponym.mapper.ExtentMapper;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.toponym.service.ExtentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ExtentServiceImpl implements ExtentService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ExtentMapper extentMapper;

    @Override
    public JSONObject findPointByExtent(String polygon,Integer scale){
        List<String> lists = extentMapper.findCategory(scale);
        List<String> category_lists = stringList(lists);
        JSONObject jsonObj = extentMapper.findPointByExtent(polygon).getJSONObject("row_to_json");
        String value = jsonObj.getString("value");
        jsonObj = JSONObject.parseObject(value);
        JSONArray features = jsonObj.getJSONArray("features");
        JSONObject result = new JSONObject();
        if(features != null) {
            Map<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();
            String tempIdStr = "";
            List<JSONObject> list = null;
            for (Object obj : features) {
                JSONObject jsonObject = (JSONObject) obj;
                tempIdStr = jsonObject.getJSONObject("properties").getString("CATEGORY_CODE");
                if (map.containsKey(tempIdStr)) {
                    list = map.get(tempIdStr);
                    list.add(jsonObject);
                } else {
                    list = new ArrayList<>();
                    list.add(jsonObject);
                    map.put(tempIdStr, list);
                }
            }
            JSONArray targetList = new JSONArray();
            Map targetMap = null;
            for (Map.Entry<String, List<JSONObject>> entry : map.entrySet()) {
                targetMap = new HashMap();
                targetMap.put("CATEGORY_CODE", entry.getKey());
                targetMap.put("features", entry.getValue());
                targetList.add(targetMap);
            }

            for (Object obj : targetList) {
                JSONObject listObject = JSONObject.parseObject(JSON.toJSONString(obj));
                JSONObject code = new JSONObject();
                code.put("type", "FeatureCollection");
                code.put("features", listObject.getJSONArray("features"));
                String category = listObject.getString("CATEGORY_CODE");
                if (category_lists.contains(category))
                    result.put(category, code);
            }
        }
        else{
            result = jsonObj;
        }
        return result;
    }

    @Override
    public JSONObject findLineByExtent(String polygon,Integer scale){
        List<String> lists = extentMapper.findCategory(scale);
        List<String> category_lists = stringList(lists);
        JSONObject jsonObj = extentMapper.findLineByExtent(polygon).getJSONObject("row_to_json");
        String value = jsonObj.getString("value");
        jsonObj = JSONObject.parseObject(value);
        JSONArray features = jsonObj.getJSONArray("features");
        JSONObject result = new JSONObject();
        if(features != null) {
            //数据分组
            Map<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();
            String tempIdStr = "";
            List<JSONObject> list = null;
            for (Object obj : features) {
                JSONObject jsonObject = (JSONObject) obj;
                tempIdStr = jsonObject.getJSONObject("properties").getString("CATEGORY_CODE");
                if (map.containsKey(tempIdStr)) {
                    list = map.get(tempIdStr);
                    list.add(jsonObject);
                } else {
                    list = new ArrayList<JSONObject>();
                    list.add(jsonObject);
                    map.put(tempIdStr, list);
                }
            }
            JSONArray targetList = new JSONArray();
            Map targetMap = null;
            for (Map.Entry<String, List<JSONObject>> entry : map.entrySet()) {
                targetMap = new HashMap();
                targetMap.put("CATEGORY_CODE", entry.getKey());
                targetMap.put("features", entry.getValue());
                targetList.add(targetMap);
            }

            for (Object obj : targetList) {
                JSONObject listObject = JSONObject.parseObject(JSON.toJSONString(obj));
                JSONObject code = new JSONObject();
                code.put("type", "FeatureCollection");
                code.put("features", listObject.getJSONArray("features"));
                String category = listObject.getString("CATEGORY_CODE");
                if (category_lists.contains(category))
                    result.put(category, code);
            }
        }
        else
        {
            result = jsonObj;
        }
        return result;
    }

    @Override
    public JSON findDoorplateByExtent(String polygon){
        JSONObject jsonObj = extentMapper.findDoorplateByExtent(polygon).getJSONObject("row_to_json");
        String value = jsonObj.getString("value");
        return JSON.parseObject(value);
    }

    @Override
    public JSONObject findSign(){
        String value =  extentMapper.findSign().getJSONObject("row_to_json").getString("value");
        return JSONObject.parseObject(value);
    }

    private static List<String> stringList(List<String> lists){
    //    List<String> lists = extentMapper.findCategory(scale);
        List<String> category_lists = new ArrayList<>();
        for (String list:lists){
            if (list.contains(",")) {
                String[] str_lists = list.split(",");
                for (String str : str_lists) {
                    category_lists.add(str);
                }
            }
            else{
                category_lists.add(list);
            }

        }
        return category_lists;
    }

}
