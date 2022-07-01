package com.horseloft.jpa.controller;

import com.horseloft.jpa.service.UserService;
import com.horseloft.jpa.vo.CodeStatusVo;
import com.horseloft.jpa.vo.RequestVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.user.UserLoginRequestVo;
import com.horseloft.jpa.vo.user.UserLoginResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Date: 2020/1/4 16:10
 * User: YHC
 * Desc: 公用接口|不需要拦截的接口
 */
@RestController
@RequestMapping("/public")
@Api(tags = "公共接口")
public class PublicController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "用户登陆 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<UserLoginResponseVo> login(@Valid @RequestBody UserLoginRequestVo params) {
        return userService.userLogin(params);
    }

    @PostMapping("/workerLogin")
    @ApiOperation(value = "工人登陆 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<UserLoginResponseVo> workerLogin(@Valid @RequestBody UserLoginRequestVo params) {
        params.setWorkerLogin(true);
        return userService.userLogin(params);
    }

    // ================ 以下非接口 =================
    @PostMapping("/code")
    @ApiOperation(value = "状态码说明", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<CodeStatusVo> codeStatus() {
        return ResponseVo.ofSuccess();
    }
}
