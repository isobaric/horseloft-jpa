package com.horseloft.jpa.advice;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Date: 2020/1/8 11:45
 * User: YHC
 * Desc: 处理请求、添加参数、获取流信息
 */
public class JpaInputMessage implements HttpInputMessage {

    private HttpHeaders headers;
    private InputStream body;

    public JpaInputMessage(HttpInputMessage inputMessage, String encode) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        this.headers = inputMessage.getHeaders();
        this.body = inputMessage.getBody();
        InputStream bodyInput = inputMessage.getBody();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        boolean array = false;
        String string;

        if (bodyInput.available() != 0) {
            string = decode(bodyInput, encode);
            boolean obj = JSONUtil.isJsonObj(string);
            array = JSONUtil.isJsonArray(string);
            if (obj) {
                jsonObject = JSONObject.parseObject(string);
                jsonObject.put("user", request.getAttribute("user"));
                jsonObject.put("userId", request.getAttribute("userId"));
                jsonObject.put("remoteAddr", request.getAttribute("remoteAddr"));
            }
            if (array) {
                jsonArray = JSONArray.parseArray(string);
                JSONObject json = (JSONObject) jsonArray.get(0);
                jsonObject.put("user", request.getAttribute("user"));
                jsonObject.put("userId", request.getAttribute("userId"));
                json.put("remoteAddr", request.getAttribute("remoteAddr"));
                if (!jsonArray.isEmpty()){
                    jsonArray.remove(0);
                }
                jsonArray.add(0, json);
            }
        } else {
            jsonObject.put("user", request.getAttribute("user"));
            jsonObject.put("userId", request.getAttribute("userId"));
            jsonObject.put("remoteAddr", request.getAttribute("remoteAddr"));
        }

        if (array && !jsonArray.isEmpty()) {
            string = jsonArray.toJSONString();
            this.body = decode(string, encode);
        }
        if (!array) {
            string = jsonObject.toJSONString();
            this.body = decode(string, encode);
        }
    }

    private InputStream decode(String body, String encode) throws IOException {
        return IOUtils.toInputStream(body, encode);
    }

    private String decode(InputStream body, String encode) throws IOException {
        return IOUtils.toString(body, encode);
    }

    @Override
    public InputStream getBody() throws IOException {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
