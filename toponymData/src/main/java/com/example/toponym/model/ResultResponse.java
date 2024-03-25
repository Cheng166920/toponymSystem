package com.example.toponym.model;

import com.alibaba.fastjson.JSONObject;
import com.example.toponym.service.BaseErrorInfoInterface;
import com.example.toponym.service.impl.ExceptionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @description: 自定义数据传输
 * @author: DT
 * @version: v1.0
 */
@Data
@Schema(name = "ResultResponse对象", description = "响应结果")
public class ResultResponse {

    @Schema(name = "code", description = "响应代码")
    private String code;

    @Schema(name = "message", description = "响应消息")
    private String message;

    @Schema(name = "result", description = "响应结果")
    private Object result;

    public ResultResponse() {
    }

    public ResultResponse(BaseErrorInfoInterface errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMsg();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * 成功
     *
     * @return
     */
    public static ResultResponse success() {
        ResultResponse rb = new ResultResponse();
        rb.setCode(ExceptionEnum.SUCCESS.getResultCode());
        rb.setMessage(ExceptionEnum.SUCCESS.getResultMsg());
        return rb;
    }

    /**
     * 成功
     * @param data
     * @return
     */
    public static ResultResponse success(Object data) {
        ResultResponse rb = new ResultResponse();
        if(ObjectUtils.isEmpty(data)){
            rb.setCode(ExceptionEnum.NO_CONTENT.getResultCode());
            rb.setMessage(ExceptionEnum.NO_CONTENT.getResultMsg());
            rb.setResult(data);
        }
        else {
            rb.setCode(ExceptionEnum.SUCCESS.getResultCode());
            rb.setMessage(ExceptionEnum.SUCCESS.getResultMsg());
            rb.setResult(data);
        }
        return rb;
    }


    /**
     * 失败
     */
    public static ResultResponse error(BaseErrorInfoInterface errorInfo) {
        ResultResponse rb = new ResultResponse();
        rb.setCode(errorInfo.getResultCode());
        rb.setMessage(errorInfo.getResultMsg());
        rb.setResult(null);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultResponse error(String code, String message) {
        ResultResponse rb = new ResultResponse();
        rb.setCode(code);
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultResponse error( String message) {
        ResultResponse rb = new ResultResponse();
        rb.setCode("-1");
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
