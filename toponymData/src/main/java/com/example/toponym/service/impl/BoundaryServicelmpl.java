package com.example.toponym.service.impl;

import com.example.toponym.mapper.BoundaryMapper;
import com.example.toponym.model.Action;
import com.example.toponym.model.BoundaryRequest;
import com.example.toponym.model.PageBean;
import com.example.toponym.service.BoundaryService;
import com.example.toponym.utils.ShapeUtil;
import com.github.pagehelper.PageHelper;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BoundaryServicelmpl implements BoundaryService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private BoundaryMapper boundaryMapper;
    //界线
    @Override
    public JSONObject findBoundary(String level){

        JSONObject jsonObj = boundaryMapper.findBoundary(level).getJSONObject("row_to_json");
        //return jsonObj;
        String value = jsonObj.getString("value");
        jsonObj = JSONObject.fromObject(value);
        JSONArray features = jsonObj.getJSONArray("features");

        Map<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();
        String tempIdStr = "";
        List<JSONObject> list = null;
        for(Object obj : features){
            JSONObject jsonObject = (JSONObject) obj;
            tempIdStr = jsonObject.getJSONObject("properties").getString("BOUNDARY_RANK");
            if(map.containsKey(tempIdStr)){
                list = map.get(tempIdStr);
                list.add(jsonObject);
            }else{
                list = new ArrayList<JSONObject>();
                list.add(jsonObject);
                map.put(tempIdStr, list);
            }
        }
        JSONArray targetList = new JSONArray();
        Map targetMap = null;
        for(Map.Entry<String, List<JSONObject>> entry : map.entrySet()){
            targetMap = new HashMap();
            targetMap.put("BOUNDARY_RANK", entry.getKey());
            targetMap.put("features", entry.getValue());
            targetList.add(targetMap);
        }

        JSONObject result = new JSONObject();
        for(Object obj : targetList){
            JSONObject listObject = (JSONObject) obj;
            JSONObject code = new JSONObject();
            code.put("type","FeatureCollection");
            code.put("features",listObject.getJSONArray("features"));
            String category = listObject.getString("BOUNDARY_RANK");
            result.put(category,code);
        }
        return result;
    }
    //界桩
    @Override
    public JSONObject findBoundaryStake(){
        String value =  boundaryMapper.findBoundaryStake().getJSONObject("row_to_json").getString("value");
        return JSONObject.fromObject(value);
    }
    //边界点
    @Override
    public JSONObject findBoundaryPoint(){
        String value =  boundaryMapper.findBoundaryPoint().getJSONObject("row_to_json").getString("value");
        return JSONObject.fromObject(value);
    }
    //三交点
    @Override
    public JSONObject findTripleNode(){
        String value =  boundaryMapper.findTripleNode().getJSONObject("row_to_json").getString("value");
        return JSONObject.fromObject(value);
    }

    @Override
    public Map<String,Object> getBoundary(String IDENTIFICATION_CODE){

        return boundaryMapper.getBoundary(IDENTIFICATION_CODE);
    }

    @Override
    public Map<String,Object> downloadRequest(String code,String user){
        Action action = new Action();
        Map<String,Object> map = getBoundary(code);
        JSONObject obj = new JSONObject();
        obj.put("IDENTIFICATION_CODE",code);
        obj.put("TYPE",map.get("TYPE"));
        obj.put("GEOM",map.get("GEOM"));
        obj.put("USER",user);
        action.setCONTENT(obj);
        boundaryMapper.addRequest(action);
        Integer gid = action.getGID();
        Map<String, Object> newAction = boundaryMapper.findByGID(gid);
        com.alibaba.fastjson.JSONObject newObj = com.alibaba.fastjson.JSONObject.parseObject(newAction.get("CONTENT").toString());
        newAction.replace("CONTENT",newAction.get("CONTENT"),newObj);
        return  newAction;
    }

    @Override
    public List<Map<String,Object>> findAllRequest(){
        List<Map<String,Object>> list = boundaryMapper.findAll();
        return list;
    }

    @Override
    public void downloadShp(Integer gid,HttpServletResponse response){
        List<Map<String, Object>> list = boundaryMapper.DownloadData(gid);
        if(list.size()>0) {
            //List<Map<String, Object>> list = getBoundary(code);
            List<String> stringList = new ArrayList<>();
            for (Map<String, Object> map : list) {
                for (String key : map.keySet()) {
                    stringList.add(key);
                }
            }
            String geoType = list.get(0).get("TYPE").toString();
            String url = "D:\\" + list.get(0).get("CODE") + ".shp";
            ShapeUtil.write2Shape(url, "utf-8", geoType, "GEOM", stringList, list);
            ShapeUtil.zipShapeFile(url);
            String zipPath = "D:\\" + list.get(0).get("CODE") + ".zip";
            // 读到流中
            //文件的存放路径
            InputStream inStream = null;
            try {
                inStream = new FileInputStream(zipPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 设置输出的格式
            ServletOutputStream outputStream = null;
            response.reset();
            //response.setContentType("application/bin");
            try {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(list.get(0).get("CODE") + ".zip", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 循环取出流中的数据
            byte[] b = new byte[1024];
            int len = 0;
            try {
                outputStream = response.getOutputStream();
                while ((len = inStream.read(b)) > 0) {
                    outputStream.write(b, 0, len);
                }
                inStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //删除临时文件
            File file = new File(zipPath);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
            boundaryMapper.updateRequest("已完成",gid);
        }
    }

    @Override
    public Map<String,Object> downloadReview(Integer gid,String review){
        boundaryMapper.updateReview(review,gid);
        Map<String, Object> newAction = boundaryMapper.findByGID(gid);
        com.alibaba.fastjson.JSONObject newObj = com.alibaba.fastjson.JSONObject.parseObject(newAction.get("CONTENT").toString());
        newAction.replace("CONTENT",newAction.get("CONTENT"),newObj);
        return newAction;
    }



    @Override
    public void deleteRequest(Integer gid){
        boundaryMapper.deleteRequest(gid);
    }


    @Override
    public byte[] getFile(String code, String fileType,HttpServletResponse response){
        String file = fileType;
        String table = boundaryMapper.getTableName(code);
        if(fileType.equals("成果图")){
            file = "GRAPH";
            table = "B_S_VILLAGE_LT";
        }
        else if(fileType.equals("协议书"))
            file = "AGREEMENT";
        else if(fileType.equals("协议书附图"))
            file = "AGREEMENT_FIGURE";
        else if(fileType.equals("边界点成果表"))
            file = "BOUNDARY_POINT_TABLE";
        if (table.equals("市级界线"))
            table = "B_S_CITY_LT";
        else if(table.equals("县级界线"))
            table = "B_S_COUNTY_LT";
        else if (table.equals("乡级界线"))
            table = "B_S_TOWN_LT";
        Map<String,Object> map =  boundaryMapper.getFile(code,file,table);
        byte[] bytes = (byte[]) map.get(file);
        try {
            String extension = ".pdf";
            if(bytes[0] == 82)
                extension = ".rar";
            String fileName = map.get("BOUNDARY_NAME") + fileType + extension;
            if (null != bytes) {
                response.reset();
                response.addHeader("Content-Disposition", "attachment; filename=" +URLEncoder.encode(fileName,"UTF-8"));
                OutputStream out = response.getOutputStream();
                out.write(bytes);
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes;
    }

    @Override
    public List<Map<String,Object>> findInfo(String type){
        String table = type;
        if(type.equals("市级界线")||type.equals("县级界线")||type.equals("乡级界线")){
            table = "市级界线、县级界线、乡级界线";
        }
        else if(type.equals("县级界桩")||type.equals("乡级界桩")){
            table = "县级界桩、乡级界桩";
        }
        String[] params = boundaryMapper.findField(table);
        List<Map<String,Object>> list = boundaryMapper.findInfo(params,type);
        for (int i = 0; i < list.size(); i++) {
            Map<String,Object> map = list.get(i);
            map.replace("GID",map.get("GID"),i+1);
            list.set(i,map);
        }
        return list;
    }

    @Override
    public PageBean<Map<String,Object>> findByName(String name,Integer currentPage,Integer pageSize){
        String type = boundaryMapper.getBoundaryType(name);
        if(type != null) {
            if (type.equals("市级界线") || type.equals("县级界线") || type.equals("乡级界线")) {
                type = "市级界线、县级界线、乡级界线";
            } else if (type.equals("县级界桩") || type.equals("乡级界桩")) {
                type = "县级界桩、乡级界桩";
            }
        }
        String[] field = boundaryMapper.findField(type);
        PageHelper.startPage(currentPage,pageSize);//后面紧跟数据库查询语句
        List<Map<String,Object>> list = boundaryMapper.findByName(field,name);
        for (int i = 0; i < list.size(); i++) {
            Map<String,Object> map = list.get(i);
           // map.replace("GID",map.get("GID"),i+1);
            map.remove("GID");
            //list.set(i,map);
        }
        PageBean<Map<String, Object>> pageBean = new PageBean<>(list);
        return pageBean;
    }

    @Override
    public Map<String,Object> findByCode(String code){
        String type = boundaryMapper.getTableName(code);
        if(type != null) {
            if (type.equals("市级界线") || type.equals("县级界线") || type.equals("乡级界线")) {
                type = "市级界线、县级界线、乡级界线";
            } else if (type.equals("县级界桩") || type.equals("乡级界桩")) {
                type = "县级界桩、乡级界桩";
            }
        }
        String[] field = boundaryMapper.findField(type);
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> boundary = boundaryMapper.findByCode(field,code);
        if(boundary != null) {
            boundary.remove("GID");
            map = boundary;
        }
        return map;
    }

    @Override
    public Map<String,Object> findByGID(Integer gid){
        return boundaryMapper.findByGID(gid);
    }
}
