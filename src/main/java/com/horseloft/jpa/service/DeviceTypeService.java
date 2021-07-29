package com.horseloft.jpa.service;

import com.horseloft.jpa.db.dao.DeviceDao;
import com.horseloft.jpa.db.dao.DeviceTypeAttrDao;
import com.horseloft.jpa.db.dao.DeviceTypeDao;
import com.horseloft.jpa.db.dao.PictureDao;
import com.horseloft.jpa.db.entity.Device;
import com.horseloft.jpa.db.entity.DeviceType;
import com.horseloft.jpa.db.entity.DeviceTypeAttr;
import com.horseloft.jpa.db.entity.Picture;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.exception.BusinessException;
import com.horseloft.jpa.utils.ParamsUtils;
import com.horseloft.jpa.utils.StringUtils;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.device.DeviceTypeAddRequestVo;
import com.horseloft.jpa.vo.device.DeviceTypeAttrVo;
import com.horseloft.jpa.vo.device.DeviceTypeDetailVo;
import com.horseloft.jpa.vo.device.DeviceTypeResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: 2020/1/29 上午10:36
 * User: YHC
 * Desc: 设备类型
 */
@Service
public class DeviceTypeService {

    @Autowired
    private DeviceTypeDao deviceTypeDao;

    @Autowired
    private DeviceTypeAttrDao deviceTypeAttrDao;

    @Autowired
    private PictureDao pictureDao;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private DeviceDao deviceDao;

    /**
     * 编辑添加 设备类型
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<ResponseIdVo> deviceTypeBuild(DeviceTypeAddRequestVo params) {
        DeviceType deviceType = deviceTypeDao.getDeviceTypeByName(params.getDeviceTypeName(), false);
        if (params.getId() > 0) {
            if (deviceType != null && !deviceType.getId().equals(params.getId())) {
                return ResponseVo.ofError("名称已存在");
            }
        } else {
            if (deviceType != null) {
                return ResponseVo.ofError("名称已存在");
            }
        }
        if (params.getId() > 0 && deviceType == null) {
            deviceType = deviceTypeDao.findDeviceTypeByIdAndDeleteStatus(params.getId(), false);
        }

        //设备类型属性|先查询当前设备类型的属性，不存在参数中则删除，存在则更新，参数中id=0则新增
        List<DeviceTypeAttrVo> attrValueList = params.getAttrValue();
        //验证提交的数据中有没有重名的
        Set<String> attrValueNameSet = new HashSet<>();
        attrValueList.forEach(x -> {
            if (StringUtils.isBlank(x.getAttrName())) {
                throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "属性值不能为空");
            }
            attrValueNameSet.add(x.getAttrName().trim());
        });
        if (attrValueNameSet.size() != attrValueList.size()) {
            return ResponseVo.ofError("属性值名称不能重复");
        }
        if (deviceType == null) {
            deviceType = new DeviceType();
        }
        //设备类型|新增、编辑
        deviceType.setRemark(params.getRemark());
        deviceType.setDeviceTypeName(params.getDeviceTypeName());
        deviceTypeDao.save(deviceType);

        List<DeviceTypeAttr> deviceTypeAttrList = deviceTypeAttrDao.getAttrsByDeviceTypeIds(params.getId());
        //属性值参数为空时 如果数据库数据不为空 则全部删除数据库数据
        if (attrValueList.isEmpty() && !deviceTypeAttrList.isEmpty()) {
            deviceTypeAttrList.forEach(x -> {
                x.setDeleteStatus(true);
                x.setAttrName(StringUtils.appendRandom(x.getAttrName()));
                deviceTypeAttrDao.save(x);
            });
        } else if (!attrValueList.isEmpty()) {
            //属性值参数不为空 数据库数据为空 则全部写入|并发时存在参数中带有id的数据 已经被删除的情况 此时不处理即可
            for (DeviceTypeAttrVo attrVo : attrValueList) {
                if (attrVo.getId() == 0) {
                    //写入
                    DeviceTypeAttr attrInert = new DeviceTypeAttr();
                    attrInert.setAttrName(attrVo.getAttrName());
                    attrInert.setDeviceTypeId(deviceType.getId());
                    attrInert.setWarnStatus(attrVo.getWarnStatus());
                    deviceTypeAttrDao.save(attrInert);
                } else {
                    //更新|只更新数据库存在的数据
                    for (DeviceTypeAttr deviceTypeAttr : deviceTypeAttrList) {
                        if (attrVo.getId().equals(deviceTypeAttr.getId())) {
                            deviceTypeAttr.setAttrName(attrVo.getAttrName());
                            deviceTypeAttr.setWarnStatus(attrVo.getWarnStatus());
                            deviceTypeAttr.setDeleteStatus(attrVo.getDeleteStatus());
                            deviceTypeAttrDao.save(deviceTypeAttr);
                        }
                    } //endFor
                } //endElse
            } //endFor
        }

        ResponseIdVo vo = new ResponseIdVo();
        vo.setId(deviceType.getId());
        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 设备类型图片添加
     * @param file
     * @param id
     * @return
     */
    public ResponseVo<ResponseIdVo> deviceTypePictureAddById(MultipartFile file, Long id) {
        if (file.isEmpty()) {
            return ResponseVo.ofError("图片不能为空");
        }
        if (!ParamsUtils.isImageType(file.getContentType())) {
            return ResponseVo.ofError("图片格式错误");
        }
        //设备类型只有一张图片
        List<Picture> pictures = pictureDao.getPicturesByBelongId(id, 1);
        if (pictures.size() >= 1) {
            return ResponseVo.ofError("只能添加一张设备类型图片");
        }
        ResponseVo<ResponseIdVo> vo = pictureService.pictureSave(file, id, 1);

        return ResponseVo.ofSuccess(vo.getData());
    }

    /**
     * 设备类型详情
     * @param params
     * @return
     */
    public ResponseVo<DeviceTypeDetailVo> getDeviceTypeDetailById(RequestIdVo params) {
        DeviceType deviceType = deviceTypeDao.findDeviceTypeByIdAndDeleteStatus(params.getId(), false);
        if (deviceType == null) {
            return ResponseVo.ofError("设备类型不存在");
        }
        //设备类型属性
        List<DeviceTypeAttr> attrList = deviceTypeAttrDao.getAttrsByDeviceTypeIds(params.getId());
        List<DeviceTypeAttrVo> attrVoList = new ArrayList<>();
        attrList.forEach(x -> {
            DeviceTypeAttrVo attrVo = new DeviceTypeAttrVo();
            BeanUtils.copyProperties(x, attrVo);
            attrVoList.add(attrVo);
        });

        DeviceTypeDetailVo vo = new DeviceTypeDetailVo();
        BeanUtils.copyProperties(deviceType, vo);
        vo.setAttrValue(attrVoList);
        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 设备类型删除|正在使用的设备类型无法删除
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<String> deviceTypeDeleteById(RequestIdVo params) {
        DeviceType deviceType = deviceTypeDao.findDeviceTypeByIdAndDeleteStatus(params.getId(), false);
        if (deviceType != null) {
            Device device = deviceDao.getDeviceByDeviceTypeId(params.getId());
            if (device != null) {
                return ResponseVo.ofError("正在使用的设备类型无法删除");

            }
            //设备类型图片、设备类型属性值不删除即可

            //删除设备类型属性
            List<DeviceTypeAttr> attrList = deviceTypeAttrDao.getAttrsByDeviceTypeIds(params.getId());
            for (DeviceTypeAttr deviceTypeAttr : attrList) {
                deviceTypeAttr.setDeleteStatus(true);
                deviceTypeAttr.setAttrName(StringUtils.appendRandom(deviceTypeAttr.getAttrName()));
                deviceTypeAttrDao.save(deviceTypeAttr);
            }

            deviceType.setDeleteStatus(true);
            deviceType.setDeviceTypeName(StringUtils.appendRandom(deviceType.getDeviceTypeName()));
            deviceTypeDao.save(deviceType);
        }
        return ResponseVo.ofSuccess();
    }

    /**
     * 设备类型列表
     * @return
     */
    public ResponseVo<List<DeviceTypeResponseVo>> getDeviceTypeList() {
        List<DeviceTypeResponseVo> list = new ArrayList<>();
        List<DeviceType> deviceTypeList = deviceTypeDao.findDeviceTypesByDeleteStatus(false);
        if (deviceTypeList.isEmpty()) {
            return ResponseVo.ofSuccess(list);
        }
        //图片
        List<Picture> pictureList = pictureDao.findPicturesByTypeAndDeleteStatus(1, false);

        for (DeviceType deviceType : deviceTypeList) {
            DeviceTypeResponseVo vo = new DeviceTypeResponseVo();
            BeanUtils.copyProperties(deviceType, vo);
            for (Picture picture : pictureList) {
                if (picture.getBelongId().equals(deviceType.getId())) {
                    vo.setPicturePath(picture.getPath());
                    break;
                }
            }
            list.add(vo);
        }

        return ResponseVo.ofSuccess(list);
    }
}
