package com.example.toponym.service.impl;

import com.example.toponym.service.BaseErrorInfoInterface;

/**
 * @description: 异常处理枚举类
 * @author: DT
 * @date: 2021/4/19 21:41
 * @version: v1.0
 */
public enum ExceptionEnum implements BaseErrorInfoInterface {

    // 数据操作错误定义
    SUCCESS("200", "请求成功。"),
    NO_CONTENT("204","请求成功，但未返回内容。"),
    BODY_NOT_MATCH("400","客户端请求的语法错误，服务器无法理解。"),
    PARAM_NOT_MATCH("4001","客户端请求的参数错误，服务器无法匹配。"),
    NULL_NOT_MATCH("4002","客户端请求的参数错误，服务器获取到的参数为空。"),
    TYPE_NOT_MATCH("4003","客户端请求的参数类型不匹配，服务器无法理解。"),
    SQL_NOT_MATCH("4004","客户端请求的SQL参数错误，查询数据库失败。"),
    FORMAT_NOT_MATCH("4005","客户端数据传输格式选择错误，服务器无法理解。"),
    REQUEST_NOT_MATCH("4006","客户端请求的方法错误，服务器无法理解。"),
    PKEY_NOT_UNIQUE("4007", "客户端请求的参数错误，重复键违反唯一约束。"),
    JSON_NOT_MATCH("4009", "客户端请求的参数错误，类型json的输入语法无效。"),
    SIGNATURE_NOT_MATCH("401","请求要求用户的身份认证。"),
    PARAMS_NOT_CONVERT("402","数字格式异常。"),
    FORBIDDEN("403", "服务器理解请求客户端的请求，但是拒绝执行此请求。"),
    NOT_FOUND("404", "服务器无法根据客户端的请求找到资源（网页）。"),
    INTERNAL_SERVER_ERROR("500", "服务器内部错误，无法完成请求。");

    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    ExceptionEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
