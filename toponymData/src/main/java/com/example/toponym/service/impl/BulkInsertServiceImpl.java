package com.example.toponym.service.impl;

import com.example.toponym.mapper.BulkInsertMapper;
import com.example.toponym.model.DoorPlateAElement;
import com.example.toponym.model.DoorPlateElement;
import com.example.toponym.service.BulkInsertService;
import com.hankcs.hanlp.model.crf.CRFSegmenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import java.util.ArrayList;

@Service
public class BulkInsertServiceImpl implements BulkInsertService {
    @Autowired
    private BulkInsertMapper bulkInsertMapper;

    @Override
    public void deleteData(){
        bulkInsertMapper.deleteData();
    }

    @Override
    public List<DoorPlateElement> findAllData(){
        return bulkInsertMapper.findAllData();
    }

    @Override
    public List<DoorPlateAElement> getApplicationData(){
        List<DoorPlateElement> doorPlateElementList = bulkInsertMapper.findAllData();
        List<DoorPlateAElement> doorPlateAElementList = new ArrayList<>();
        for (DoorPlateElement element:doorPlateElementList
             ) {
            DoorPlateAElement aElement = toFtsFormat(element);
            doorPlateAElementList.add(aElement);
        }
        return doorPlateAElementList;
    }

    @Override
    public void insertSourceTable(){
       // List<DoorPlateElement> doorPlateElementList = bulkInsertMapper.findAllData();
        bulkInsertMapper.insertSourceTable();
    }

    @Override
    public void insertApplicationTable(){
//        List<DoorPlateElement> doorPlateElementList = bulkInsertMapper.findAllData();
//        List<DoorPlateAElement> doorPlateAElementList = new ArrayList<>();
//        for (DoorPlateElement element:doorPlateElementList
//        ) {
//            DoorPlateAElement aElement = toFtsFormat(element);
//            doorPlateAElementList.add(aElement);
//        }
        bulkInsertMapper.insertApplicationTable();
    }

    @Override
    public void writeApplicationData(String filePath){
        List<DoorPlateElement> doorPlateElementList = bulkInsertMapper.findAllData();
        List<DoorPlateAElement> list = new ArrayList<>();
        for (DoorPlateElement element:doorPlateElementList
        ) {
            DoorPlateAElement aElement = toFtsFormat(element);
            list.add(aElement);
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            // 写入表头

            writer.write("uid, fts_word, fts_char");
            writer.newLine();
            // 遍历列表中的每个Map
            for (Object dataMap : list) {
                Class<?> clazz = dataMap.getClass();
                StringBuilder line = new StringBuilder();
                // 遍历Map中的每个键值对，并将其写入文件
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true); // 设置字段可访问
                    try {
                        String key = field.getName();
                        Object value = field.get(dataMap); // 获取字段的值
                        // 将键值对写入文件，使用适当的分隔符
                        if (value != null) {
                            line.append(value);
                        }
                        if (!key.equals("fts_char")) { // 如果不是最后一个字段，则添加逗号
                            line.append(",");
                        }
                        //writer.write(key + ": " + value + "\t"); // 这里使用制表符分隔
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
                writer.write(line.toString());
                writer.newLine(); // 写入换行符，将不同的Map数据分隔开
            }
            writer.close();
            System.out.println("数据已成功写入文件：" + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void writeData(String filePath){
        List<DoorPlateElement> list = bulkInsertMapper.findAllData();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            // 写入表头

            writer.write("uid, province, city, district, township, village, road, road_direction, estate, building, unit, doorplate, doorplate_sub, room, house_license, license_time, category, owner, owner_phone, construction, principal, principal_phone, collect_time, linear_toponym, point_toponym, geom, remarks, photo_name");
            writer.newLine();
            // 遍历列表中的每个Map
            for (Object dataMap : list) {
                Class<?> clazz = dataMap.getClass();
                StringBuilder line = new StringBuilder();
                // 遍历Map中的每个键值对，并将其写入文件
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true); // 设置字段可访问
                    try {
                        String key = field.getName();
                        Object value = field.get(dataMap); // 获取字段的值
                        // 将键值对写入文件，使用适当的分隔符
                        if (value != null) {
                            line.append(value);
                        }
                        if (!key.equals("photo_name")) { // 如果不是最后一个字段，则添加逗号
                            line.append(",");
                        }
                        //writer.write(key + ": " + value + "\t"); // 这里使用制表符分隔
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
                writer.write(line.toString());
                writer.newLine(); // 写入换行符，将不同的Map数据分隔开
            }
            writer.close();
            System.out.println("数据已成功写入文件：" + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Long> selectMaxUID(List<Map<String,Object>> list){
        return bulkInsertMapper.selectMaxUID(list);
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
}
