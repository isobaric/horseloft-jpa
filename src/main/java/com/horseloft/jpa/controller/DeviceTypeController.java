package com.horseloft.jpa.controller;

import com.horseloft.jpa.service.DeviceTypeService;
import com.horseloft.jpa.service.PictureService;
import com.horseloft.jpa.vo.PictureResponseVo;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.device.DeviceTypeAddRequestVo;
import com.horseloft.jpa.vo.device.DeviceTypeDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Date: 2020/1/28 下午5:16
 * User: YHC
 * Desc:
 */
@RestController
@RequestMapping("/deviceType")
@Api(tags = "设备类型")
public class DeviceTypeController {

    @Autowired
    private DeviceTypeService deviceTypeService;

    @Autowired
    private PictureService pictureService;

    @PostMapping("/add")
    @ApiOperation(value = "设备类型添加/编辑 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseIdVo> deviceTypeAddAndEdit(@Valid @RequestBody DeviceTypeAddRequestVo params) {
        return deviceTypeService.deviceTypeBuild(params);
    }

    @PostMapping("/pictureAdd")
    @ApiOperation(value = "设备类型图片添加 [已完成]", httpMethod = "POST" ,produces = "application/octet-stream")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "上传的文件key", required = true, dataType = "file", dataTypeClass = MultipartFile.class, paramType = "form"),
            @ApiImplicitParam(name = "id", value = "设备类型id", required = true, dataType = "Long", dataTypeClass = Long.class)
    })
    public ResponseVo<ResponseIdVo> deviceTypePictureAdd(@RequestParam("file") MultipartFile file, @Valid @Min(value = 1) @RequestParam("id") Long id) {
        return deviceTypeService.deviceTypePictureAddById(file, id);
    }

    @PostMapping("/getPicture")
    @ApiOperation(value = "设备类型图片获取 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<PictureResponseVo> getDeviceTypePicture(@Valid @RequestBody RequestIdVo params) {
        return pictureService.getOnePicture(params.getId(), 1);
    }

    @PostMapping("/pictureDel")
    @ApiOperation(value = "设备类型图片删除 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> deviceTypePictureDelete(@Valid @RequestBody RequestIdVo params) {
        return pictureService.pictureDeleteById(params.getId(), 1);
    }

    @PostMapping("/detail")
    @ApiOperation(value = "设备类型详情 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<DeviceTypeDetailVo> getDeviceTypeDetail(@Valid @RequestBody RequestIdVo params) {
        return deviceTypeService.getDeviceTypeDetailById(params);
    }

    @PostMapping("/del")
    @ApiOperation(value = "设备类型删除 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> deviceTypeDelete(@Valid @RequestBody RequestIdVo params) {
        return deviceTypeService.deviceTypeDeleteById(params);
    }
}
