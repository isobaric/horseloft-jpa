package com.horseloft.jpa.controller;

import com.horseloft.jpa.annotation.Structure;
import com.horseloft.jpa.enums.JpaDao;
import com.horseloft.jpa.service.DeviceService;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseListVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.device.DeviceAddRequestVo;
import com.horseloft.jpa.vo.device.DeviceListRequestVo;
import com.horseloft.jpa.vo.device.DeviceListResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Date: 2020/1/28 下午2:12
 * User: YHC
 * Desc: 设备
 */
@RestController
@RequestMapping("/device")
@Api(tags = "设备")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Structure(JpaDao.DEFAULT)
    @PostMapping("/add")
    @ApiOperation(value = "设备添加/编辑 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseIdVo> deviceAddAndEdit(@Valid @RequestBody DeviceAddRequestVo params) {
        return deviceService.deviceBuild(params);
    }

    @Structure(JpaDao.DEVICE)
    @PostMapping("/detail")
    @ApiOperation(value = "设备详情 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<DeviceAddRequestVo> getDeviceDetail(@Valid @RequestBody RequestIdVo params) {
        return deviceService.getDeviceDetailById(params);
    }

    @Structure(JpaDao.DEVICE)
    @PostMapping("/del")
    @ApiOperation(value = "设备删除 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> deviceDelete(@Valid @RequestBody RequestIdVo params) {
        return deviceService.deleteDeviceById(params);
    }

    @PostMapping("/list")
    @ApiOperation(value = "设备列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseListVo<DeviceListResponseVo>> getDeviceList(@Valid @RequestBody DeviceListRequestVo params) {
        return deviceService.deviceListForCurrentUser(params);
    }

    @PostMapping("/download")
    @ApiOperation(value = "设备列表导出 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public void getDeviceListDownload(@Valid @RequestBody DeviceListRequestVo params, HttpServletResponse response) throws IOException {
        deviceService.deviceListDownload(params, response);
    }
}
