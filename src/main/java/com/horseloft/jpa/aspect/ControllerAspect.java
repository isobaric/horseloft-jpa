package com.horseloft.jpa.aspect;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.horseloft.jpa.constant.UserConstant;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.utils.DateUtils;
import com.horseloft.jpa.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Date: 2020/1/4 13:38
 * User: YHC
 * Desc:
 */
@Slf4j
@Aspect
@Component
public class ControllerAspect {
    //方法名
    private ThreadLocal<String> methodName = new ThreadLocal<>();

    //开始时间
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    //Trace
    private ThreadLocal<String> traceId = new ThreadLocal<>();

    @Pointcut("execution( * com.horseloft..controller..*Controller.*(..))")
    public void controllerPointCut() {

    }

    @Before("controllerPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        String string;
        startTime.set(System.currentTimeMillis());
        methodName.set(method);
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects
                    .requireNonNull(RequestContextHolder.getRequestAttributes()))
                    .getRequest();
            //跳过文件上传 下载
            if (ServletUtil.isMultipart(request)) {
                string = "文件上传";
            } else if (request.getRequestURI().toLowerCase().contains("/download")) {
                string = "文件下载";
            } else {
                Object[] getParameter = joinPoint.getArgs();
                string = JSON.toJSONString(getParameter);
                JSONArray jsonObject = JSON.parseArray(string);
                if (!jsonObject.isEmpty() && jsonObject.get(0) instanceof JSONObject) {
                    String token = ((JSONObject) jsonObject.get(0)).getString("token");
                    if (StringUtils.isNotBlank(token)) {
                        traceId.set(token);
                    }
                    String pwd = ((JSONObject) jsonObject.get(0)).getString("password");
                    if (StringUtils.isNotBlank(pwd)) {
                        Object object = getParameter[0];
                        Class<?> cls = object.getClass();
                        if (!cls.getName().contains("java.")) {
                            Field field = cls.getDeclaredField("password");
                            field.setAccessible(true);
                            //密码加密日志不可见|对称加密
                            field.set(object, TokenUtils.tokenEncode(String.valueOf(field.get(object)), UserConstant.PASSWORD_AES_KEY));
                        }
                    }
                }
                string = JSON.toJSONString(joinPoint.getArgs());
            }

            log.info("执行 {} TraceId : {} 请求参数 : {}", methodName.get(), traceId.get(), string);
        } catch (Exception e) {
            log.info("执行 {} TraceId : {} 请求参数 : {}", methodName.get(), traceId.get(), joinPoint.getArgs(), e);
        }
    }

    @After("controllerPointCut()")
    public void doAfter() {

        log.info("请求耗时 {} TraceId : {} 耗时为 : {}ms", methodName.get(), traceId.get(), System.currentTimeMillis() - startTime.get());
        methodName.remove();
        startTime.remove();
        traceId.remove();
    }

    @AfterReturning(returning = "object", pointcut = "controllerPointCut()")
    public void doAfterReturning(JoinPoint joinPoint, Object object) {
        try {
            String string = JSON.toJSONString(object);
            //服务异常的返回特殊处理
            if (string.contains(ResponseCode.SERVER_ERROR.getMessage())) {
                String title = "时间:" + DateUtils.getDateTimePatternConnect();
                String content = title + ",方法:" + methodName.get() + "参数:" + JSON.toJSONString(joinPoint.getArgs());
                log.info("ERROR::::请求参数 : {}", content);
                //TODO 异常通知
            }
            log.info("请求成功 {} TraceId : {} 响应结果 : {}", methodName.get(), traceId.get(), string);
        } catch (Exception e) {
            log.info("请求异常 {} TraceId : {} 响应结果 : {}", methodName.get(), traceId.get(), object, e);
        }
    }
}
