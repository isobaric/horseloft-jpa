package com.horseloft.jpa.controller;

import com.horseloft.jpa.annotation.Structure;
import com.horseloft.jpa.enums.JpaDao;
import com.horseloft.jpa.service.UserPictureService;
import com.horseloft.jpa.service.UserService;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseListVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.user.UserAddRequestVo;
import com.horseloft.jpa.vo.user.UserIdPictureResponseVo;
import com.horseloft.jpa.vo.user.UserListRequestVo;
import com.horseloft.jpa.vo.user.UserListResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Date: 2020/1/5 18:18
 * User: YHC
 * Desc: 用户模块
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserPictureService userPictureService;

    @PostMapping("/add")
    @Structure(JpaDao.DEFAULT)
    @ApiOperation(value = "新增/编辑账号 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseIdVo> userAdd(@Valid @RequestBody UserAddRequestVo params) {
        return userService.userInfoAdd(params);
    }

    @PostMapping("/detail")
    @Structure(JpaDao.USER)
    @ApiOperation(value = "账号详情 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<UserAddRequestVo> getUserDetail(@Valid @RequestBody RequestIdVo params) {
        return userService.getUserDetailById(params);
    }

    @PostMapping("/del")
    @ApiOperation(value = "删除账号 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> userDelete(@Valid @RequestBody RequestIdVo params) {
        return userService.userDeleteByIds(params);
    }

    @PostMapping("/list")
    @ApiOperation(value = "账号列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseListVo<UserListResponseVo>> getUserList(@Valid @RequestBody UserListRequestVo params) {
        return userService.userListByCurrentUser(params);
    }

    @PostMapping("/download")
    @ApiOperation(value = "列表导出 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public void getUserListDownload(@Valid @RequestBody UserListRequestVo params, HttpServletResponse response) throws IOException {
        userService.userListDataDownload(params, response);
    }

    @Structure(JpaDao.USER)
    @PostMapping("/jobStatusChange")
    @ApiOperation(value = "在职状态变更 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> userJobStatusChange(@Valid @RequestBody RequestIdVo params) {
        return userService.userJobStatusChangeById(params);
    }

    @Structure(JpaDao.USER)
    @PostMapping("/accountStatusChange")
    @ApiOperation(value = "账号状态变更 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> userAccountStatusChange(@Valid @RequestBody RequestIdVo params) {
        return userService.userAccountStatusChangeById(params);
    }

    @PostMapping("/pictureAdd")
    @ApiOperation(value = "身份证照片上传 [已完成]", httpMethod = "POST" ,produces = "application/octet-stream")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "上传的文件key", required = true, dataType = "file", dataTypeClass = MultipartFile.class, paramType = "form"),
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long", dataTypeClass = Long.class)
    })
    public ResponseVo<ResponseIdVo> userPictureAdd(@RequestParam("file") MultipartFile file, @RequestParam("id") Long userId) {
        return userPictureService.userPictureUpload(file, userId);
    }

    @PostMapping("/pictureDel")
    @ApiOperation(value = "身份证照片删除 [已完成]", httpMethod = "POST" ,produces = "application/octet-stream")
    public ResponseVo<String> userPictureDelete(@Valid @RequestBody RequestIdVo params) {
        return userPictureService.userPictureDeleteById(params);
    }

    @PostMapping("/pictureList")
    @ApiOperation(value = "身份证照片列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<List<UserIdPictureResponseVo>> getUserPictureList(@Valid @RequestBody RequestIdVo params) {
        return userPictureService.userPictureListById(params);
    }
}
