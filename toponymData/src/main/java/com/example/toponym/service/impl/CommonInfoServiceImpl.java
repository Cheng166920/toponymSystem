package com.example.toponym.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.toponym.common.BizException;
import com.example.toponym.mapper.ActionMapper;
import com.example.toponym.mapper.CommonInfoMapper;
import com.example.toponym.model.CommonInfo;
import com.example.toponym.model.PageBean;
import com.example.toponym.model.page.CommonInfoPage;
import com.example.toponym.service.CommonInfoService;
import com.example.toponym.utils.PdfUtils;
import com.github.pagehelper.PageHelper;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.model.crf.CRFSegmenter;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Service
public class CommonInfoServiceImpl implements CommonInfoService {
    @Autowired
    private CommonInfoMapper commonInfoMapper;
    @Autowired
    private ActionMapper actionMapper;
    //模糊检索
    @Override
    public List<Map<String, Object>> ftsQuery(String query,Integer currentPage,Integer pageSize) {
        String[] params = commonInfoMapper.findPublicField();
        List<Map<String, Object>> ftsResult = null;
        try {
            PageHelper.startPage(currentPage,pageSize);
            ftsResult = commonInfoMapper.selectFtsWord(params,StringToWord(query));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert ftsResult != null;
        if(ftsResult.isEmpty()) {
            PageHelper.startPage(currentPage, pageSize);
            ftsResult = commonInfoMapper.selectFtsChar(params, StringToCharList(query));
        }
//        for (int i = 0; i < ftsResult.size(); i++) {
//            Map<String,Object> map = new HashMap<>();
//            map.put("GID",i+1);
//            map.putAll(ftsResult.get(i));
//            ftsResult.set(i,map);
//        }
     //   PageBean<Map<String, Object>> pageBean = new PageBean<>(ftsResult);
        return ftsResult;
    }

    @Override
    public List<CommonInfo> findByJson(CommonInfo commonInfo){
        List<CommonInfo> commonInfoList = commonInfoMapper.findByJson(commonInfo);
        return commonInfoList;
    }

    @Override
    public List<String> getWord(){
        List<String> sites = commonInfoMapper.getWord();
        List<String> words = new ArrayList<String>();
        CRFLexicalAnalyzer segment = null;
        try {
            segment = new CRFLexicalAnalyzer(HanLP.Config.CRFCWSModelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert segment != null;
        segment.enablePartOfSpeechTagging(true);
        for (int i = 0; i < sites.size(); i++) {
            String site = sites.get(i);
            if(site == null) {
                words.add(null);
                continue;
            }
            String word = segment.seg(site).toString();
            word = word.substring(1, word.length()-1).replaceAll(","," ");
            words.add(word);
        }
        return words;
    }
    @Override
    public List<Map<String, Object>> findInfo(@Param("commonInfo") CommonInfo commonInfo){
        String[] params = commonInfoMapper.findPublicField();
        return commonInfoMapper.findInfo(params,commonInfo);
    }

    @Override
    public PageBean<Map<String,Object>> findInfoPage(CommonInfoPage commonInfoPage){
        String[] params = commonInfoMapper.findPublicField();
        Integer currentPage = commonInfoPage.getCurrentPage();
        Integer pageSize = commonInfoPage.getPageSize();
        CommonInfo commonInfo = commonInfoPage.getCommonInfo();
        if(!ObjectUtils.isEmpty(commonInfo.getSPECIAL_INFORMATION()))
            commonInfo.setSPECIAL_INFORMATION(JSONObject.fromObject(commonInfo.getSPECIAL_INFORMATION()));
        String toponymic_category = commonInfoPage.getCommonInfo().getTOPONYMIC_CATEGORY();
        if(toponymic_category != null) {
            String category_type = null;
            Map<String, String> map = actionMapper.getCategoryCode(toponymic_category);
            if (map == null)
                throw new BizException("4012", "客户端提交的参数错误，请检查地名类别参数的正确性。");
            for (String key : map.keySet()) {
                String value = map.get(key);
                if (value != null)
                    category_type = key;
            }
            switch (category_type) {
                case "category":
                    category_type = "门类";
                    break;
                case "large_category":
                    category_type = "大类";
                    break;
                case "medium_category":
                    category_type = "中类";
                    break;
                case "small_category":
                    category_type = "小类";
                    break;
                case "detailed_category":
                    category_type = "细目类";
                    break;
                default:
                    throw new BizException("4012", "客户端提交的参数错误，请检查地名类别参数的正确性。");
            }
            JSONObject category = new JSONObject();
            category.put(category_type,toponymic_category);
            commonInfo.setCATEGORY_NAME(category);
        }
        PageHelper.startPage(currentPage,pageSize);//后面紧跟数据库查询语句
        List<Map<String, Object>> commonInfo_list = commonInfoMapper.findInfo(params,commonInfo);
//        for (int i = 0; i < commonInfo_list.size(); i++) {
//            Map<String,Object> map = new HashMap<>();
//            map.put("GID",i+1);
//            map.putAll(commonInfo_list.get(i));
//            commonInfo_list.set(i,map);
//        }
        PageBean<Map<String, Object>> pageBean = new PageBean<>(commonInfo_list);
        return pageBean;
    }

    @Override
    public void addCommonInfo(CommonInfo commonInfo){
        commonInfoMapper.addCommonInfo(toFtsFormat(commonInfo));
    }
    @Override
    public Map<String, String> findByIDENTIFICATION_CODE(String IDENTIFICATION_CODE){
        List<CommonInfo> commonInfo_list = commonInfoMapper.findByIDENTIFICATION_CODE(IDENTIFICATION_CODE);
        Map<String,String> map = new HashMap<>();
        if(commonInfo_list.size()>0) {
            map = (Map<String, String>) commonInfo_list.get(0);
            String category = map.get("CATEGORY_CODE").toString();
            com.alibaba.fastjson.JSONObject obj =  com.alibaba.fastjson.JSON.parseObject(map.get("SPECIAL_INFORMATION").toString());
            com.alibaba.fastjson.JSONObject o = new com.alibaba.fastjson.JSONObject();
            for(Map.Entry<String, Object> entry : obj.entrySet()) {
                String k  = entry.getKey();
                k = commonInfoMapper.getFieldExplain(k,category);
                if(k == null)
                    k = entry.getKey();
                if(entry.getValue() != null) {
                    String v = entry.getValue().toString();
                    o.put(k, v);
                }
                else
                    o.put(k,"");
            }
            map.replace("SPECIAL_INFORMATION",map.get("SPECIAL_INFORMATION"),o.toString());

        }
        return map;
    }
    @Override
    public void deleteCommonInfo(String IDENTIFICATION_CODE){commonInfoMapper.deleteCommonInfo(IDENTIFICATION_CODE);}
    @Override
    public  void updateCommonInfo(CommonInfo commonInfo){
        commonInfoMapper.updateCommonInfo(commonInfo);
        CommonInfo updateResult = commonInfoMapper.findByIDENTIFICATION_CODE(commonInfo.getIDENTIFICATION_CODE()).get(0);
        commonInfoMapper.updateCommonInfo(toFtsFormat(updateResult));
    }

    @Override
    public List<Map<String,Object>> getCategoryTree(){
        List<String> list = toHashSet(commonInfoMapper.getCategory("门类"));
        List<Map<String,Object>> category_tree = new ArrayList<>();
        for (String str:list) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("label",str);
            List<String> large_list = toHashSet(commonInfoMapper.getNextCategory("大类","门类",str));
            JSONArray large_child =  new JSONArray();
            for (String large_str:large_list) {
                Map<String,Object> large_map = new LinkedHashMap<>();
                large_map.put("label",large_str);
                List<String> medium_list = toHashSet(commonInfoMapper.getNextCategory("中类","大类",large_str));
                JSONArray medium_child =  new JSONArray();
                for (String medium_str:medium_list) {
                    Map<String,Object> medium_map = new LinkedHashMap<>();
                    medium_map.put("label",medium_str);
                    List<String> small_list = toHashSet(commonInfoMapper.getNextCategory("小类","中类",medium_str));
                    JSONArray small_child =  new JSONArray();
                    for (String small_str:small_list) {
                        Map<String,Object> small_map = new LinkedHashMap<>();
                        small_map.put("label",small_str);
                        List<String> detailed_list = toHashSet(commonInfoMapper.getNextCategory("细目类","小类",small_str));
                        JSONArray detailed_child =  new JSONArray();
                        for (String detailed_str:detailed_list) {
                            Map<String,Object> detailed_map = new LinkedHashMap<>();
                            detailed_map.put("label",detailed_str);
                            detailed_child.add(detailed_map);
                        }
                        if(!detailed_list.isEmpty())
                            small_map.put("children",detailed_child);
                        small_child.add(small_map);
                    }
                    if(!small_list.isEmpty())
                        medium_map.put("children",small_child);
                    medium_child.add(medium_map);
                }
                large_map.put("children",medium_child);
                large_child.add(large_map);
            }
            map.put("children",large_child);
            category_tree.add(map);
        }
        return category_tree;
    }

    @Override
    public List<Map<String,Object>> getSpecialField(String category){
        return commonInfoMapper.getSpecialField(category);
    }

    @Override
    public byte[] getRegisterTable(Map<String,String> map, HttpServletResponse response){

        String templatePath = "/static/register/register.pdf";
        try {
            String fileName = map.get("STANDARD_NAME") + ".pdf";
            response.reset();
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName,"UTF-8"));
            OutputStream out = response.getOutputStream();
            byte[] bytes = PdfUtils.toponymPdfout(templatePath,map,out);
            out.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CommonInfo> selectAll(){
        List<CommonInfo> list = commonInfoMapper.selectAll();
        for (CommonInfo commonInfo:list
             ) {
            CommonInfo newCommonInfo = toFtsFormat(commonInfo);
            commonInfoMapper.updateCommonInfo(newCommonInfo);
        }

        return list;
    }

    private static List<String> toHashSet(List<String> list){
        HashSet h_list = new HashSet(list);
        list.clear();
        h_list.remove(null);
        for (Object h:h_list) {
            String category = (String) h;
            if(category.contains("，"))
            {
                String[] split = category.split("，");
                list.addAll(Arrays.asList(split));
            }
            else
                list.add(category);
        }
        return list;
    }

    private static CommonInfo toFtsFormat(CommonInfo commonInfo){
        StringBuilder builder = new StringBuilder();
        builder.append(commonInfo.getSTANDARD_NAME()).
               // append(commonInfo.getTOPONYMIC_CATEGORY()).
               // append(commonInfo.getESTABLISHMENT_YEAR()).
                append(commonInfo.getTOPONYM_ORIGIN()).
                append(commonInfo.getTOPONYM_MEANING()).
                append(commonInfo.getTOPONYM_HISTORY()).
                append(commonInfo.getADMINISTRATIVE_DISTRICT());
        CommonInfo newCommonInfo = new CommonInfo();
        BeanUtils.copyProperties(commonInfo,newCommonInfo);
        newCommonInfo.setFTS_CHAR(StringToCharList(builder.toString()).replaceAll("&"," "));
        try {
            newCommonInfo.setFTS_WORD(StringToWord(builder.toString()).replaceAll("&"," "));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newCommonInfo;
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
