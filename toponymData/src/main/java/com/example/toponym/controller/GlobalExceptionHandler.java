package com.example.toponym.controller;


import com.example.toponym.common.BizException;
import com.example.toponym.model.ResultResponse;
import com.example.toponym.service.impl.ExceptionEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.ws.rs.ForbiddenException;

/**
 * @description: 自定义异常处理
 * @version: v1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResultResponse bizExceptionHandler(HttpServletRequest req, BizException e){
        logger.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return ResultResponse.error(e.getErrorCode(),e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, NullPointerException e){
        logger.error("发生空指针异常！原因是:",e);
        return ResultResponse.error(ExceptionEnum.NULL_NOT_MATCH);
    }


    /**
     * 处理类型转换异常
     * @param req
     * @param e
     * @return
     */

    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    @ExceptionHandler(value = NumberFormatException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, NumberFormatException e){
        logger.error("发生数字格式异常！原因是:",e);
        return ResultResponse.error(ExceptionEnum.PARAMS_NOT_CONVERT);
    }

    /**
     * 参数请求异常
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value =IllegalStateException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, IllegalStateException e){
        logger.error("请求参数异常！原因是:",e);
        return ResultResponse.error(ExceptionEnum.PARAM_NOT_MATCH);
    }

    /**
     * 参数请求异常
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value =HttpMessageNotReadableException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, HttpMessageNotReadableException e){
        logger.error("请求参数异常！原因是:",e);
        return ResultResponse.error(ExceptionEnum.PARAM_NOT_MATCH);
    }

    /**
     * 类型json的输入语法无效
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, DataIntegrityViolationException e){
        logger.error("请求参数异常！原因是:",e);
        return ResultResponse.error(ExceptionEnum.JSON_NOT_MATCH);
    }


    /**
     * 请求方法异常
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value =HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, HttpRequestMethodNotSupportedException e){
        logger.error("请求方式异常！原因是:",e);
        return ResultResponse.error(ExceptionEnum.REQUEST_NOT_MATCH);
    }

    /**
     * SQL参数异常
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value =BadSqlGrammarException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, BadSqlGrammarException e){
        logger.error("请求的sql参数异常！原因是:",e);
        return ResultResponse.error(ExceptionEnum.SQL_NOT_MATCH);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value =UncategorizedSQLException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, UncategorizedSQLException e){
        logger.error("请求的sql参数异常！原因是:",e);
        return ResultResponse.error(ExceptionEnum.SQL_NOT_MATCH);
    }

    /**
     * 数据传输格式异常
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //@ResponseStatus(code = HttpStatus.NO_CONTENT,value = HttpStatus.NO_CONTENT)
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, HttpMediaTypeNotSupportedException e){
        logger.error("数据传输格式选择错误！原因是:",e);
        return ResultResponse.error(ExceptionEnum.FORMAT_NOT_MATCH);
    }

    /**
     * 数据传输格式异常
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, MethodArgumentTypeMismatchException e){
        logger.error("客户端请求的参数类型不匹配！原因是:",e);
        return ResultResponse.error(ExceptionEnum.TYPE_NOT_MATCH);
    }

    /**
     * 用户权限异常
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ShiroException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, ShiroException e){
        logger.error("用户权限异常！原因是:",e);
        return ResultResponse.error(ExceptionEnum.SIGNATURE_NOT_MATCH);
    }

    /**
     * 用户权限异常
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = ForbiddenException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, ForbiddenException e){
        logger.error("拒绝执行请求！原因是:",e);
        return ResultResponse.error(ExceptionEnum.FORBIDDEN);
    }

    /**
     * 用户权限异常
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, NoHandlerFoundException e){
        logger.error("未找到页面！原因是:",e);
        return ResultResponse.error(ExceptionEnum.NOT_FOUND);
    }

    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, Exception e){
        logger.error("未知异常！原因是:",e);
        return ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理重复键违反唯一约束异常
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, DuplicateKeyException e){
        logger.error("发生重复键违反唯一约束异常！原因是:",e);
        return ResultResponse.error(ExceptionEnum.PKEY_NOT_UNIQUE);
    }

    private class ShiroException extends Throwable {
    }
}

