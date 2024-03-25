package com.example.toponym.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.toponym.common.BizException;
import com.example.toponym.mapper.ActionMapper;
import com.example.toponym.mapper.CommonInfoMapper;
import com.example.toponym.model.Action;
import com.example.toponym.model.CommonInfo;
import com.example.toponym.model.ToponymHistory;
import com.example.toponym.service.ActionService;
import com.example.toponym.utils.RandomUtil;
import com.hankcs.hanlp.model.crf.CRFSegmenter;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ActionServiceImpl implements ActionService {
    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private CommonInfoMapper commonInfoMapper;

    @Override
    public Boolean isValid(Action action){
        JSONObject input_content = JSONObject.parseObject(action.getCONTENT().toString());
        String operation = action.getACTION();

            if (input_content.containsKey("ADMINISTRATIVE_DISTRICT")) {
                String administration = input_content.getString("ADMINISTRATIVE_DISTRICT");
                try {
                    StringBuilder builder = new StringBuilder();
                    builder.append(administration);
                    String word = StringToWord(builder.toString()).replaceAll("&", " ");
                    String[] split = word.split(" ");
                    if (split.length < 4) {
                        if(operation.equals("业务新增")||operation.equals("公众申报")) {
                            throw new BizException("4011", "客户端提交的参数错误，请检查所跨行政区划参数的正确性。");
                        }
                    }
                    if (split.length > 4) {
                        String town = split[3];
                        for (int i = 4;i < split.length; i++) {
                            town = town + split[i];
                        }
                        split[3] = town;

                    }
                    if(split.length >= 4) {
                        String[] admin = new String[4];
                        for (int i = 0; i < 4; i++) {
                            admin[i] = split[i];
                        }
                        String township_code = null;
                        for (String str : admin) {
                            if (str == admin[0])
                                township_code = actionMapper.getTownshipCode("0", str);
                            else
                                township_code = actionMapper.getTownshipCode(township_code, str);
                        }
                        if (township_code == null)
                            throw new BizException("4011", "客户端提交的参数错误，请检查所跨行政区划参数的正确性。");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(input_content.containsKey("TOPONYMIC_CATEGORY")){
                String toponymic_category = input_content.getString("TOPONYMIC_CATEGORY");
                String category_code = null;
                Map<String,String> map = actionMapper.getCategoryCode(toponymic_category);
                if(map == null)
                    throw new BizException("4012", "客户端提交的参数错误，请检查地名类别参数的正确性。");
                for (String value : map.values()) {
                    if(value != null )
                        category_code = value;
                }
                if(category_code != null){
                    boolean valid = actionMapper.codeIsValid(category_code);
                    if(!valid)
                        throw new BizException("4012", "客户端提交的参数错误，请检查地名类别参数的正确性。");
                }
            }
            if(input_content.containsKey("ESTABLISHMENT_YEAR")){
                String year = input_content.getString("ESTABLISHMENT_YEAR");
                if(year.length()>20)
                    throw new BizException("4013", "客户端提交的参数错误，请检查设立年份参数的正确性。");
            }

        String input_code = input_content.getString("IDENTIFICATION_CODE");
        Boolean validity = actionMapper.isValid(input_code);
        if (operation.equals("业务新增")||operation.equals("公众申报"))
            validity = !validity;
        return validity;
    }

    @Override
    public Map<String, Object> addAction(Action action){
        JSONObject content = JSON.parseObject(String.valueOf(action.getCONTENT()));
        String operation = action.getACTION();
        if(operation.equals("业务修改") ||operation.equals("公众纠错")||operation.equals("业务删除")) {
            List<CommonInfo> commonInfo= commonInfoMapper.findByIDENTIFICATION_CODE(content.getString("IDENTIFICATION_CODE"));
           // JSONObject commonInfo_obj = JSONObject.fromObject(commonInfo.get(0));
        //    if(operation.equals("业务删除")){
            JSONObject commonInfo_obj = JSONObject.parseObject(JSON.toJSONString(commonInfo.get(0)));
            String code = commonInfo_obj.getString("TOPONYM_CODE");
            if(content.containsKey("TOPONYM_CODE"))
                content.remove("TOPONYM_CODE");
            content.put("TOPONYM_CODE",code);
       //     }
//            else {
//                CommonInfo spe = commonInfoMapper.findByCode(content.getString("IDENTIFICATION_CODE"));
//                JSONObject o =  JSONObject.parseObject(spe.getSPECIAL_INFORMATION().toString());
//                //Map<String,Object> info = (Map<String, Object>) commonInfo.get(0);
//                //com.alibaba.fastjson.JSONObject o =  com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(info.get("SPECIAL_INFORMATION")));
//                JSONObject commonInfo_obj = JSONObject.parseObject(JSON.toJSONString(commonInfo.get(0)));
//               // com.alibaba.fastjson.JSONObject o =  com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(commonInfo_obj.getString("SPECIAL_INFORMATION")));
//                commonInfo_obj.remove("SPECIAL_INFORMATION");
//                for(Map.Entry<String, Object> entry : o.entrySet()){
//                    String k = entry.getKey();
//                    String v = o.getString(k);
//                    commonInfo_obj.put(k, v);
//                }
//                for(Map.Entry<String, Object> entry : content.entrySet()){
//                    String key = entry.getKey();
//                    String value = content.getString(key);
//                    commonInfo_obj.remove(key);
//                    commonInfo_obj.put(key, value);
//                }
//                content = JSONObject.parseObject(String.valueOf(commonInfo_obj));
//            }
        }
        if(!operation.equals("业务删除")){
            //添加分词字段
            if(content.containsKey("STANDARD_NAME"))
                content = JSONObject.parseObject(String.valueOf(toFtsFormat(content)));
        }
        if(content.containsKey("GEOM")) {
            JSONObject geom = content.getJSONObject("GEOM");
            content.replace("GEOM",content.getString("GEOM"),geom);
        }
        action.setCONTENT(content);
        actionMapper.addAction(action);
        Integer toponym_gid = action.getGID();
        Map<String, Object> newAction = actionMapper.findByGID(toponym_gid);
        JSONObject obj = JSONObject.parseObject(newAction.get("CONTENT").toString());
        newAction.replace("CONTENT",newAction.get("CONTENT"),obj);
        return newAction;
    }

    @Override
    public Map<String, Object> updateReview(Action action){
        Map<String,Object> action_list = actionMapper.findByGID(action.getGID());
        ToponymHistory history = new ToponymHistory();
        String update_action = action_list.get("ACTION").toString();
        history.setUPDATE_ACTION(update_action);
        JSONObject content = JSONObject.parseObject(action_list.get("CONTENT").toString());
        String[] split = new String[3];
        if(content.containsKey("ADMINISTRATIVE_DISTRICT")) {
            String administration = content.getString("ADMINISTRATIVE_DISTRICT");
            try {
                StringBuilder builder = new StringBuilder();
                builder.append(administration);
                String word = StringToWord(builder.toString()).replaceAll("&"," ");
                split = word.split(" ");
                if(split.length >= 4)
                    content.replace("ADMINISTRATIVE_DISTRICT",administration,split[3]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(update_action.equals("公众申报")||update_action.equals("业务新增")){
            String review = action.getREVIEW();
            if(review.equals("审核通过"))
            {
                String toponymic_category = content.getString("TOPONYMIC_CATEGORY");
                String category_code = null;
                Map<String,String> map = actionMapper.getCategoryCode(toponymic_category);
                for (String value : map.values()) {
                    if(value != null )
                        category_code = value;
                }
                content.put("CATEGORY_CODE",category_code);
               // String toponym_code = actionMapper.getToponymCode(content.getString("ADMINISTRATIVE_DISTRICT")).substring(0,9);
                String township_code = null;
                if(!content.containsKey("TOPONYM_CODE")) {
                    for (String str : split) {
                        if (str == split[0])
                            township_code = actionMapper.getTownshipCode("0", str);
                        else
                            township_code = actionMapper.getTownshipCode(township_code, str);
                    }
                    String toponym_code = township_code.substring(0, 9);
                    toponym_code = toponym_code + category_code;
                    List<String> code_list = actionMapper.getCodeList(category_code);
                    if (code_list.size() > 0) {
                        List<Integer> sequence_list = new ArrayList<>();
                        for (String str : code_list) {
                            Integer number = Integer.valueOf(str.substring(str.length() - 6));
                            sequence_list.add(number);
                        }
                        Integer nextSequence = Collections.max(sequence_list) + 1;
                        Integer number = nextSequence;
                        Integer bit = 0;
                        do {
                            nextSequence = nextSequence / 10;
                            bit = bit + 1;
                        } while (nextSequence > 0);
                        for (bit = 6 - bit; bit > 0; bit--) {
                            toponym_code = toponym_code + "0";
                        }
                        toponym_code = toponym_code + number;

                    } else {
                        toponym_code = toponym_code + "000001";
                    }
                    content.put("TOPONYM_CODE", toponym_code);
                }
                //添加地名分类字段
                content = JSONObject.parseObject(getCategoryJson(content).toString());
            }
        }
        if(content.containsKey("ADMINISTRATIVE_DISTRICT")){
            action.setCONTENT(content);
            action.setACTION(update_action);
            actionMapper.updateAction(action);
        }
        history.setIDENTIFICATION_CODE(content.getString("IDENTIFICATION_CODE"));
        JSONObject new_content = content;
        JSONObject old_content = null;
        Integer num = commonInfoMapper.findByIDENTIFICATION_CODE(content.getString("IDENTIFICATION_CODE")).size();
        if(num>0) {
            switch (update_action) {
                case "业务删除":
                    new_content = null;
                case "业务修改":
                case "公众纠错":
                    List<CommonInfo> commonInfo = commonInfoMapper.findByIDENTIFICATION_CODE(content.getString("IDENTIFICATION_CODE"));
                    old_content = JSONObject.parseObject(JSON.toJSONString(commonInfo.get(0)));
                    break;
            }

            history.setNEW_CONTENT(new_content);
            history.setOLD_CONTENT(old_content);
        }
        actionMapper.updateReview(action);
        if(!update_action.equals("业务删除") || num>0)
            actionMapper.addHistory(history);
        Map<String, Object> newAction = actionMapper.findByGID(action.getGID());
        com.alibaba.fastjson.JSON obj = com.alibaba.fastjson.JSON.parseObject(String.valueOf(newAction.get("CONTENT")));
        newAction.replace("CONTENT",newAction.get("CONTENT"),obj);
        return newAction;
    }

    @Override
    public void updateAction(Action action) {
        JSONObject newContent = JSONObject.parseObject(action.getCONTENT().toString());
        Map<String,Object> map = actionMapper.findByGID(action.getGID());
        List<String> action_list = Arrays.asList("业务新增","业务删除","业务修改","公众申报","公众纠错");
        if(!action_list.contains(action.getACTION())){
            action.setACTION((String) map.get("ACTION"));
        }
        JSONObject content = JSONObject.parseObject(map.get("CONTENT").toString());
        if(newContent != null) {
            for(Map.Entry<String, Object> entry : newContent.entrySet()){
                String key = entry.getKey();
                String value = newContent.getString(key);
                content.remove(key);
                content.put(key, value);
            }
        }
        action.setCONTENT(toFtsFormat(content));
        actionMapper.updateAction(action);
    }
    public void deleteAction(Integer gid){actionMapper.deleteAction(gid);}
    @Override
    public List<Map<String,Object>> findAll(){
        List<Map<String,Object>> list = actionMapper.findAll();
        for (Map<String,Object> action:list
             ) {
            JSON obj = JSON.parseObject(String.valueOf(action.get("CONTENT")));
            action.replace("CONTENT",action.get("CONTENT"),obj);
        }
        return list;
    }

    @Override
    public Map<String,Object> findByGID(Integer gid){
        Map<String, Object> action = actionMapper.findByGID(gid);
        JSON obj = JSON.parseObject(String.valueOf(action.get("CONTENT")));
        action.replace("CONTENT",action.get("CONTENT"),obj);
       // JSONObject obj = JSONObject.fromObject(action.get("CONTENT"));
       // action.replace("CONTENT",action.get("CONTENT"),obj);
        return action;
    }

    @Override
    public Map<String,Object> findUpdateByGID(Integer gid){
        Map<String, Object> action = actionMapper.findByGID(gid);
        JSONObject obj = JSONObject.parseObject(String.valueOf(action.get("CONTENT")));
        action.replace("CONTENT",action.get("CONTENT"),obj);
        String action_type = action.get("ACTION").toString();
        if (action_type.equals("公众纠错")||action_type.equals("业务修改")) {
            JSONObject new_content = new JSONObject();
            new_content.putAll(obj);
            String id = new_content.getString("IDENTIFICATION_CODE");
            Map<String, Object> old_content = (Map<String, Object>) commonInfoMapper.findByIDENTIFICATION_CODE(id).get(0);
            try {
                new_content.remove("USER");
                new_content.remove("CONTACT_NUMBER");
                new_content.remove("ORGANIZATION");
                new_content.remove("FTS_CHAR");
                new_content.remove("FTS_WORD");
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONArray update_field = new JSONArray();
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
            action.put("UPDATE_FIELD",update_field);
        }
        // JSONObject obj = JSONObject.fromObject(action.get("CONTENT"));
        // action.replace("CONTENT",action.get("CONTENT"),obj);
        return action;
    }

    @Override
    public String setIdentificationCode(){
        String code = RandomUtil.getRandomNumber8() + "-" + RandomUtil.getRandomNumber4() + "-" + RandomUtil.getRandomNumber4()+ "-" + RandomUtil.getRandomNumber4()+ "-" + RandomUtil.getRandomNumber12();
        return code;
    }

    private static Object toFtsFormat(com.alibaba.fastjson.JSONObject content){
        StringBuilder builder = new StringBuilder();
        builder.append(content.getString("STANDARD_NAME")).
           //     append(content.getString("TOPONYMIC_CATEGORY")).
          //      append(content.getString("ESTABLISHMENT_YEAR")).
                  append(" ").
                append(content.getString("TOPONYM_ORIGIN")).
                append(content.getString("TOPONYM_MEANING")).
                append(content.getString("TOPONYM_HISTORY")).
                append(content.getString("ADMINISTRATIVE_DISTRICT"));
        JSONObject newContent = new JSONObject();
        newContent.putAll(content);
        String fts_char = StringToCharList(builder.toString()).replaceAll("&"," ");
        newContent.put("FTS_CHAR",fts_char);
        try {
            String fts_word = StringToWord(builder.toString()).replaceAll("&"," ");
            newContent.put("FTS_WORD",fts_word);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newContent;
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

    public Object getCategoryJson(JSONObject content){
        JSONObject category = new JSONObject();
        String toponym_code = content.getString("TOPONYM_CODE").substring(9,14);
        String category_code = toponym_code.substring(0,1) + "0000";
        String category_name = actionMapper.getCategoryName("category",category_code);
        category.put("门类",category_name);
        String largeCategory_code = toponym_code.substring(0,2) + "000";
        String largeCategory_name = actionMapper.getCategoryName("large_category",largeCategory_code);
        category.put("大类",largeCategory_name);
        String mediumCategory_code = toponym_code.substring(0,3) + "00";
        String mediumCategory_name = actionMapper.getCategoryName("medium_category",mediumCategory_code);
        category.put("中类",mediumCategory_name);
        String smallCategory_code = "";
        String detailedCategory_code = "";
        if(!toponym_code.substring(3,3).equals("0")) {
            smallCategory_code = toponym_code.substring(0, 4) + "0";
            String smallCategory_name = actionMapper.getCategoryName("small_category",smallCategory_code);
            category.put("小类",smallCategory_name);
            if(!toponym_code.substring(4,4).equals("0")) {
                detailedCategory_code = toponym_code.substring(0, 5);
                String detailedCategory_name = actionMapper.getCategoryName("detailed_category",detailedCategory_code);
                category.put("细目类",detailedCategory_name);
            }
        }
        content.put("CATEGORY_NAME",category);
        return content;
    }


}
