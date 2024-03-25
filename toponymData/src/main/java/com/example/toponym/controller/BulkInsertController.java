//package com.example.toponym.controller;
//
//import com.example.toponym.common.BizException;
//import com.example.toponym.model.DoorPlateAElement;
//import com.example.toponym.model.DoorPlateElement;
//import com.example.toponym.model.ResultResponse;
//import com.example.toponym.service.BulkInsertService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv.CSVRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.*;
//
//@Slf4j
//@RestController
//@Tag(name = "批量更新模块",description = "上传数据文件，批量插入到数据库中")
//@RequestMapping("/insert")
//public class BulkInsertController {
//    @Autowired
//    private BulkInsertService bulkInsertService;
//
//    @Operation(summary = "检索")
//    @GetMapping("/source_list")
//    public ResultResponse findAllData(){
//        List<DoorPlateElement> list = bulkInsertService.findAllData();
//        return ResultResponse.success(list);
//    }
//
//    @Operation(summary = "应用数据表信息")
//    @GetMapping("/application_list")
//    public ResultResponse getApplicationData(){
//        List<DoorPlateAElement> list = bulkInsertService.getApplicationData();
//        return ResultResponse.success(list);
//    }
//
//    @Operation(summary = "写入源数据表")
//    @GetMapping("/source_insert")
//    public ResultResponse insertSourceTable(){
//        bulkInsertService.insertSourceTable();
//        return ResultResponse.success();
//    }
//
//    @Operation(summary = "写入应用数据表")
//    @GetMapping("/application_insert")
//    public ResultResponse insertApplicationTable(){
//        bulkInsertService.insertApplicationTable();
//        return ResultResponse.success();
//    }
//
//    @Operation(summary = "源数据写入文件")
//    @GetMapping("/source_write")
//    public ResultResponse writeSourceData(){
//        String filePath = "C:/source_data.txt"; // 输出文件的路径
//        bulkInsertService.writeData(filePath);
//        return ResultResponse.success();
//    }
//
//    @Operation(summary = "应用数据写入文件")
//    @GetMapping("/application_write")
//    public ResultResponse writeApplicationData(){
//        String filePath = "C:/application_data.txt"; // 输出文件的路径
//        bulkInsertService.writeApplicationData(filePath);
//        return ResultResponse.success();
//
//    }
//    @Operation(summary = "上传源文件")
//    @PostMapping("/uploadCsv")
//    public ResultResponse uploadCsv(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            // 处理文件为空的情况
//            throw new BizException("4011","客户端上传的文件为空。");
//        }
//
//        try (CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(new InputStreamReader(file.getInputStream()))) {
//            List<Map<String, Object>> dataList = new ArrayList<>();
//
//            for (CSVRecord record : csvParser) {
//                Map<String, Object> data = new HashMap<>();
//                for (String header : csvParser.getHeaderMap().keySet()) {
//                    data.put(header, record.get(header));
//                }
//                dataList.add(data);
//            }
//            List<Long> maxUID = bulkInsertService.selectMaxUID(dataList);
//
//            // 将数据插入数据库，使用你的MyBatis Mapper
//            //yourMapper.insertCsvData(dataList);
//
//            // 数据处理完成后的重定向或其他操作
//            return ResultResponse.success(maxUID);
//        } catch (IOException e) {
//            e.printStackTrace();
//            // 处理文件读取异常
//            throw new BizException("4012","文件读取异常。");
//        }
//    }
//}
