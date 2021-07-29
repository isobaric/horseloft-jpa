package com.horseloft.jpa.advice;

import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.exception.BusinessException;
import com.horseloft.jpa.vo.ResponseVo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 2020/1/4 13:23
 * User: YHC
 * Desc: 统一错误处理
 */
@ControllerAdvice
public class ExceptionHandleAdvice {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseVo<String> handlerException(HttpServletRequest request, Exception e) {
        ResponseVo<String> responseVo = new ResponseVo<>();
        if (e instanceof BusinessException) {
            //自定义异常
            responseVo.setCode(((BusinessException) e).getErrorCode());
            responseVo.setMessage(e.getMessage());
        } else if (e instanceof HttpMessageNotReadableException) {
            //http异常|空参数
            responseVo.setCodeMessage(ResponseCode.PARAMETER_ERROR);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            //不支持的请求方式
            responseVo.setCodeMessage(ResponseCode.METHOD_ERROR);
        } else if (e instanceof DataIntegrityViolationException) {
            //字段转换异常
            responseVo.setCodeMessage(ResponseCode.DATA_ERROR);
        } else {
            //e.getMessage()
            //其他异常|开发环境可以将异常信息输出
            responseVo.setCodeMessage(ResponseCode.SERVER_ERROR);
        }

        e.printStackTrace();

        return responseVo;
    }
}
