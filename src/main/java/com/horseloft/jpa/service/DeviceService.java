package com.horseloft.jpa.service;

import com.horseloft.jpa.db.dao.DeviceDao;
import com.horseloft.jpa.db.dao.DeviceTypeAttrDao;
import com.horseloft.jpa.db.dao.DeviceTypeAttrValueDao;
import com.horseloft.jpa.db.dao.DeviceTypeDao;
import com.horseloft.jpa.db.entity.Device;
import com.horseloft.jpa.db.entity.DeviceType;
import com.horseloft.jpa.db.entity.DeviceTypeAttr;
import com.horseloft.jpa.db.entity.DeviceTypeAttrValue;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.exception.BusinessException;
import com.horseloft.jpa.utils.FileUtils;
import com.horseloft.jpa.utils.NumberUtils;
import com.horseloft.jpa.utils.StringUtils;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseListVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.device.DeviceAddRequestVo;
import com.horseloft.jpa.vo.device.DeviceListRequestVo;
import com.horseloft.jpa.vo.device.DeviceListResponseVo;
import com.horseloft.jpa.vo.device.DeviceTypeAttrValueVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Date: 2020/2/1 上午11:05
 * User: YHC
 * Desc: 设备
 */
@Service
public class DeviceService {

    @Autowired
    private PublicService publicService;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private DeviceTypeDao deviceTypeDao;

    @Autowired
    private DeviceTypeAttrDao deviceTypeAttrDao;

    @Autowired
    private DeviceTypeAttrValueDao deviceTypeAttrValueDao;

    /**
     * 设备新增 编辑
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<ResponseIdVo> deviceBuild(DeviceAddRequestVo params) {
        //编号 名称不能重复
        Device deviceCodeData = deviceDao.findDeviceByDeviceCodeAndDeleteStatus(params.getDeviceCode(), false);
        Device deviceNameData = deviceDao.findDeviceByDeviceNameAndDeleteStatus(params.getDeviceName(), false);
        if (params.getId() > 0) {
            //编辑
            if (deviceCodeData != null && !deviceCodeData.getId().equals(params.getId())) {
                return ResponseVo.ofError("编号已存在");
            }
            if (deviceNameData != null && !deviceNameData.getId().equals(params.getId())) {
                return ResponseVo.ofError("名称已存在");
            }
        } else {
            //新增
            if (deviceCodeData != null) {
                return ResponseVo.ofError("编号已存在");
            }
            if (deviceNameData != null) {
                return ResponseVo.ofError("名称已存在");
            }
        }
        Device device = new Device();
        if (params.getId() > 0) {
            device = deviceDao.findDeviceByIdAndDeleteStatus(params.getId(), false);
            if (device == null) {
                return ResponseVo.ofError("设备信息不存在");
            }
        }

        //设备类型
        DeviceType deviceType = deviceTypeDao.findDeviceTypeByIdAndDeleteStatus(params.getDeviceTypeId(), false);
        if (deviceType == null) {
            return ResponseVo.ofError("设备类型不存在");
        }

        //只有设备类型的属性没有被删除，则参数deviceTypeAttr中的值有效，允许deviceTypeAttr值为空
        List<DeviceTypeAttrValueVo> attrValueList = new ArrayList<>();
        List<DeviceTypeAttr> attrList = deviceTypeAttrDao.getAttrsByDeviceTypeIds(params.getDeviceTypeId());
        if (!attrList.isEmpty() && !params.getDeviceTypeAttr().isEmpty()) {
            List<Long> attrIdList = new ArrayList<>();
            attrList.forEach(x -> attrIdList.add(x.getId()));
            //过滤掉请求参数中值为空的，保留参数中存在于 attrList 中的属性值
            for (DeviceTypeAttrValueVo valueVo : params.getDeviceTypeAttr()) {
                if (StringUtils.isNoneEmpty(valueVo.getAttrValue()) && attrIdList.contains(valueVo.getId())) {
                    attrValueList.add(valueVo);
                }
            }
        }

        device.setCompanyId(params.getCompanyId());
        device.setFactoryId(params.getFactoryId());
        device.setWorkshopId(params.getWorkshopId());
        device.setDeviceCode(params.getDeviceCode());
        device.setDeviceName(params.getDeviceName());
        device.setDeviceTypeId(params.getDeviceTypeId());
        device.setDeviceState(params.getDeviceState());
        device.setMoveStatus(params.getMoveStatus());
        device.setPassStatus(params.getPassStatus());
        device.setRemark(params.getRemark());
        deviceDao.save(device);

        //允许修改设备类型 属性值使用全删全增处理|删除表 jpa_device_type_attr_value 中的旧的设备类型信息 然后添加新的设备类型信息
        if (params.getId() > 0) {
            deviceTypeAttrValueDao.deleteAttrValueByDeviceId(params.getId());
        }
        //属性值写入
        for (DeviceTypeAttrValueVo value : attrValueList) {
            DeviceTypeAttrValue val = new DeviceTypeAttrValue();
            val.setDeviceId(device.getId());
            val.setDeviceTypeAttrId(value.getId());
            val.setAttrValue(value.getAttrValue());
            deviceTypeAttrValueDao.save(val);
        }

        ResponseIdVo vo = new ResponseIdVo();
        vo.setId(device.getId());
        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 设备详情
     * @param params
     * @return
     */
    public ResponseVo<DeviceAddRequestVo> getDeviceDetailById(RequestIdVo params) {
        Device device = deviceDao.findDeviceByIdAndDeleteStatus(params.getId(), false);
        if (device == null) {
            return ResponseVo.ofError("设备信息不存在");
        }
        DeviceAddRequestVo response = new DeviceAddRequestVo();
        BeanUtils.copyProperties(device, response);

        //设备类型的属性
        List<DeviceTypeAttrValueVo> attrValueVoList = new ArrayList<>();
        List<DeviceTypeAttr> attrList = deviceTypeAttrDao.getAttrsByDeviceTypeIds(device.getDeviceTypeId());
        if (!attrList.isEmpty()) {
            //当前设备的属性值
            List<Long> attrIds = new ArrayList<>();
            attrList.forEach(x -> attrIds.add(x.getId()));
            List<DeviceTypeAttrValue> valueList = deviceTypeAttrValueDao.getValuesByDeviceIdAndAttrIds(device.getId(), attrIds);
            for (DeviceTypeAttrValue value : valueList) {
                DeviceTypeAttrValueVo valueVo = new DeviceTypeAttrValueVo();
                valueVo.setId(value.getDeviceTypeAttrId());
                valueVo.setAttrValue(value.getAttrValue());
                attrValueVoList.add(valueVo);
            }
        }
        response.setDeviceTypeAttr(attrValueVoList);

        return ResponseVo.ofSuccess(response);
    }

    /**
     * 设备删除|工厂地图上使用的设备无法删除
     * @param params
     * @return
     */
    public ResponseVo<String> deleteDeviceById(RequestIdVo params) {
        Device device = deviceDao.findDeviceByIdAndDeleteStatus(params.getId(), false);
        if (device != null) {
            if (device.getAxisX() > 0 || device.getAxisY() > 0) {
                return ResponseVo.ofError("设备使用中，无法删除");
            }
            device.setDeviceName(StringUtils.appendRandom(device.getDeviceName()));
            device.setDeviceCode(StringUtils.appendRandom(device.getDeviceCode()));
            device.setDeleteStatus(true);
            deviceDao.save(device);
        }
        return ResponseVo.ofSuccess();
    }

    /**
     * 设备导出
     * @param params
     */
    public void deviceListDownload(DeviceListRequestVo params, HttpServletResponse response) throws IOException {
        List<Device> deviceList = deviceDao.findAll(this.deviceSpecification(params));
        if (deviceList.isEmpty()) {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "您下载的数据为空");
        }
        List<DeviceListResponseVo> list = this.deviceListMixer(deviceList, true);
        //下载
        FileUtils.excelDownload(response, list, "设备列表");
    }

    /**
     * 设备列表|区分当前登录用户所属组织结构
     * @param params
     * @return
     */
    public ResponseVo<ResponseListVo<DeviceListResponseVo>> deviceListForCurrentUser(DeviceListRequestVo params) {
        //分页 排序
        Sort sort = Sort.by(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), sort);
        Page<Device> devicePage = deviceDao.findAll(this.deviceSpecification(params), pageable);

        ResponseListVo<DeviceListResponseVo> responseListVo = new ResponseListVo<>();
        responseListVo.setPage(params.getPage());
        responseListVo.setPageSize(params.getPageSize());
        responseListVo.setTotal(devicePage.getTotalElements());
        responseListVo.setTotalPage(devicePage.getTotalPages());
        responseListVo.setList(this.deviceListMixer(devicePage.getContent(), false));

        return ResponseVo.ofSuccess(responseListVo);
    }

    //设备列表查询条件
    private Specification<Device> deviceSpecification(DeviceListRequestVo params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            //查询条件
            list.add(criteriaBuilder.equal(root.get("deleteStatus").as(Integer.class), 0));
            //组织结构
            PublicService publicService = new PublicService();
            Map<String, Object> map = publicService.getSearchStructure(params.getUser(), params.getCompanyId(), params.getFactoryId(), params.getWorkshopId());
            if (!map.isEmpty()) {
                list.add(criteriaBuilder.equal(root.get(map.get("name").toString()).as(Long.class), map.get("value")));
            }
            //设备类型
            if (NumberUtils.isActiveLong(params.getDeviceTypeId())) {
                list.add(criteriaBuilder.equal(root.get("deviceTypeId").as(Long.class), params.getDeviceTypeId()));
            }
            //设备状态
            if (NumberUtils.isNaturalNumber(params.getDeviceState()) && params.getDeviceState() < 3) {
                list.add(criteriaBuilder.equal(root.get("deviceState").as(Integer.class), params.getDeviceState()));
            }

            //搜索框
            List<Predicate> listOr = new ArrayList<>();
            if (StringUtils.isNoneEmpty(params.getSearchValue())) {
                listOr.add(criteriaBuilder.equal(root.get("deviceCode").as(String.class), params.getSearchValue()));
                listOr.add(criteriaBuilder.equal(root.get("deviceName").as(String.class), params.getSearchValue()));
            }

            Predicate[] predicatesAnd = new Predicate[list.size()];
            if (listOr.isEmpty()) {
                return criteriaBuilder.and(list.toArray(predicatesAnd));
            } else {
                Predicate[] predicatesOr = new Predicate[listOr.size()];
                Predicate predicateAnd = criteriaBuilder.and(list.toArray(predicatesAnd));
                Predicate predicateOr = criteriaBuilder.and(listOr.toArray(predicatesOr));

                return query.where(predicateAnd, predicateOr).getRestriction();
            }
        };
    }

    //设备列表数据整合
    private List<DeviceListResponseVo> deviceListMixer(List<Device> deviceList, boolean isDownload) {
        List<DeviceListResponseVo> list = new ArrayList<>();
        if (deviceList.isEmpty()) {
            return list;
        }
        //设备类型|设备必有设备类型
        Set<Long> deviceTypeIds = new HashSet<>();
        deviceList.forEach(x -> deviceTypeIds.add(x.getDeviceTypeId()));
        List<DeviceType> deviceTypeList = deviceTypeDao.findDeviceTypesByIdInAndDeleteStatus(new ArrayList<>(deviceTypeIds), false);

        //公司 工厂 车间 数据集合
        Map<String, List<?>> listMap = publicService.getStructureDataByList(deviceList);

        for (Device device : deviceList) {
            DeviceListResponseVo vo = new DeviceListResponseVo();
            BeanUtils.copyProperties(device, vo);
            //组织结构
            String structureName = publicService.getStructureName(listMap, device.getCompanyId(), device.getFactoryId(), device.getWorkshopId());
            vo.setStructure(structureName);

            //设备类型名称
            for (DeviceType deviceType : deviceTypeList) {
                if (device.getDeviceTypeId().equals(deviceType.getId())) {
                    vo.setDeviceType(deviceType.getDeviceTypeName());
                    break;
                }
            }

            if (isDownload) {
                vo.setLockStatusText(vo.getLockStatus() ? "是" : "否");
                vo.setMoveStatusText(vo.getMoveStatus() ? "是" : "否");
                vo.setPassStatusText(vo.getPassStatus() ? "是" : "否");
                if (vo.getDeviceState().equals(1)) {
                    vo.setDeviceStateText("正常");
                } else if (vo.getDeviceState().equals(2)) {
                    vo.setDeviceStateText("待维修");
                } else {
                    vo.setDeviceStateText("不可用");
                }
                //设备坐标
                if (device.getAxisX() > 0 || device.getAxisY() > 0) {
                    if (device.getWalkingStatus()) {
                        vo.setDeviceAxis("移动中");
                    } else {
                        vo.setDeviceAxis("X (" + device.getAxisX() + ") Y (" + device.getAxisY() + ")");
                    }
                }
            }
            list.add(vo);
        }

        return list;
    }
}
