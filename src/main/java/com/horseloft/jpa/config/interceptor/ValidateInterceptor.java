package com.horseloft.jpa.config.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.horseloft.jpa.constant.RoleConstant;
import com.horseloft.jpa.db.dao.ResourceNodeDao;
import com.horseloft.jpa.db.dao.RoleDao;
import com.horseloft.jpa.db.dao.UserDao;
import com.horseloft.jpa.db.entity.ResourceNode;
import com.horseloft.jpa.db.entity.Role;
import com.horseloft.jpa.db.entity.User;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.exception.BusinessException;
import com.horseloft.jpa.utils.ConvertUtils;
import com.horseloft.jpa.utils.DateUtils;
import com.horseloft.jpa.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: 2020/1/4 13:56
 * User: YHC
 * Desc: 请求验证/拦截
 */
@Slf4j
public class ValidateInterceptor implements HandlerInterceptor {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    ResourceNodeDao resourceNodeDao;

    private User user;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //将远程IP,user-agent信息加密写入通用参数
        request.setAttribute("remoteAddr", this.getRealIP(request));

        // 如果请求为静态资源请求时，类型转换会报错 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.getDeclaringClass().isAnnotationPresent(RestController.class) && this.isAccessToken(request)) {
            return this.isAccessPower(request, handlerMethod);
        }
        return true;
    }

    //token验证
    private boolean isAccessToken (HttpServletRequest request) {
        //token验证
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(ResponseCode.TOKEN_EXPIRE);
        }

        //获取token用户
        User user = userDao.getActiveUserByToken(token);
        if (user == null) {
            throw new BusinessException(ResponseCode.TOKEN_EXPIRE);
        }

        //登录是否过期
        if (System.currentTimeMillis() >= user.getExpireTime()) {
            throw new BusinessException(ResponseCode.TOKEN_EXPIRE);
        }

        //token解密
        String decodeToken = TokenUtils.tokenDecode(token, user.getSecretKey());
        if (StringUtils.isBlank(decodeToken)) {
            throw new BusinessException(ResponseCode.TOKEN_EXPIRE);
        }
        JSONObject tokenObject;
        try {
            tokenObject = JSONObject.parseObject(decodeToken);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.TOKEN_EXPIRE);
        }
        //token参数验证
        if (tokenObject.get("userId") == null) {
            throw new BusinessException(ResponseCode.TOKEN_EXPIRE);
        }
        Long userId = Long.valueOf(String.valueOf(tokenObject.get("userId")));

        //数据异常
        if (!userId.equals(user.getId())) {
            throw new BusinessException(ResponseCode.TOKEN_EXPIRE);
        }

        //更新用户的登录时间
        long expireTime;
        if (user.getAutoLoginStatus() == 1) {
            expireTime = DateUtils.getMillisTimeAfterDay(7);
        } else {
            expireTime = DateUtils.getMillisTimeAfterHour(24);
        }
        userDao.updateUserExpireTime(userId, expireTime);

        //设置
        request.setAttribute("userId", userId);
        request.setAttribute("user", user);

        this.user = user;

        return true;
    }

    //权限验证
    private boolean isAccessPower(HttpServletRequest request, HandlerMethod handlerMethod) {
        //超级管理员角色
        if (this.user.getRoleId().contains(RoleConstant.ADMIN_ROLE_ID_STR)) {
            return true;
        }

        //当前路由是否在权限范围内|允许/common/**路由
        String uri = request.getRequestURI();
        if (uri.isEmpty()) {
            return false;
        }
        if (uri.length() >= 8 && "common".equalsIgnoreCase(uri.substring(1, 7))) {
            return true;
        }

        //需要密码已修改
        if (!this.user.getPasswordStatus()) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED);
        }

        //用户角色
        List<Role> roleList = roleDao.findRolesByIdInAndDeleteStatus(ConvertUtils.strToListLong(user.getRoleId(), ","), false);
        if (roleList.isEmpty()) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED);
        }
        //角色读写权限区分
        Set<Long> readWriteSet = new HashSet<>();
        Set<Long> nodeSet = new HashSet<>();
        roleList.forEach(x -> {
            List<Long> nodeIds = ConvertUtils.strToListLong(x.getRoleNode(), ",");
            nodeSet.addAll(nodeIds);
            //读写权限
            if (!nodeIds.isEmpty() && x.getRolePower() == 1) {
                readWriteSet.addAll(nodeIds);
            }
        });
        if (nodeSet.isEmpty()) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED);
        }

        //当前路由对应的|只验证一级路由，即controller层|jpa_resource_node 表id
        int index = uri.substring(1).indexOf("/");
        if (index == -1) {
            return false;
        }
        ResourceNode node = resourceNodeDao.getInfoByBackendRoute(uri.substring(0, index + 1).toLowerCase());
        if (node == null || !nodeSet.contains(node.getId())) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED);
        }

        //读写权限验证|Controller的方法体以get开头 为读权限，非get开头 为写权限
        Method method = handlerMethod.getMethod();
        String methodName = method.getName().toLowerCase();
        if (!methodName.startsWith("get") && (readWriteSet.isEmpty() || !readWriteSet.contains(node.getId()))) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED);
        }

        return true;
    }

    /**
     * 获取IP
     * @param request
     * @return
     */
    public String getRealIP(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("X-Real-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                    try {
                        InetAddress net = InetAddress.getLocalHost();
                        ipAddress = net.getHostAddress();
                    } catch (UnknownHostException e) {
                        ipAddress = "127.0.0.1";
                    }
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "127.0.0.2";
        }
        return ipAddress;
    }
}
