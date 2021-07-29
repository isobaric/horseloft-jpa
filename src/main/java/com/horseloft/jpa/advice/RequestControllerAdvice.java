package com.horseloft.jpa.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.horseloft.jpa.exception.BusinessException;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;

/**
 * Date: 2020/1/4 13:31
 * User: YHC
 * Desc: Controller的增强处理
 */
@ControllerAdvice
public class RequestControllerAdvice implements RequestBodyAdvice {

    /**
     * 判断是否执行当前Advice
     * @param methodParameter
     * @param type
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * 读取参数前执行
     * @param httpInputMessage
     * @param methodParameter
     * @param type
     * @param aClass
     * @return
     * @throws IOException
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        try {
            return new JpaInputMessage(httpInputMessage, "UTF-8");
        } catch (Exception e) {
            return httpInputMessage;
        }
    }

    /**
     * 读取参数后执行
     * @param body
     * @param httpInputMessage
     * @param methodParameter
     * @param type
     * @param aClass
     * @return
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        //通用验证
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> set = validator.validate(body, Default.class);
        Iterator<ConstraintViolation<Object>> iterator = set.iterator();
        if (iterator.hasNext()) {
            ConstraintViolation<Object> vl = iterator.next();
            if ("不能为null".equals(vl.getMessage())) {
                throw new BusinessException("500", vl.getPropertyPath() + "" + vl.getMessage());
            }
            throw new BusinessException("500", vl.getMessage());
        }

        return body;
    }

    /**
     * 处理空请求|空body未触发该方法
     * @param body
     * @param httpInputMessage
     * @param methodParameter
     * @param type
     * @param aClass
     * @return
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        try {
            String bodyString = IOUtils.toString(new JpaInputMessage(httpInputMessage, "UTF-8").getBody());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(bodyString, Class.forName(type.getTypeName()));
        } catch (Exception e) {
            return body;
        }
    }
}
