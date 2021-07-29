package com.horseloft.jpa.service;

import com.horseloft.jpa.db.dao.FactoryDao;
import com.horseloft.jpa.db.dao.UserDao;
import com.horseloft.jpa.db.dao.WorkshopDao;
import com.horseloft.jpa.db.entity.Factory;
import com.horseloft.jpa.db.entity.User;
import com.horseloft.jpa.db.entity.Workshop;
import com.horseloft.jpa.utils.ParamsUtils;
import com.horseloft.jpa.utils.StringUtils;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.structure.WorkshopAddRequestVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date: 2020/1/18 16:02
 * User: YHC
 * Desc: 车间
 */
@Service
public class WorkshopService {

    @Autowired
    private WorkshopDao workshopDao;

    @Autowired
    private FactoryDao factoryDao;

    @Autowired
    private UserDao userDao;

    /**
     * 新增、编辑车间信息
     * @param params
     * @return
     */
    public ResponseVo<ResponseIdVo> workshopEditAndAdd(WorkshopAddRequestVo params) {
        //工厂下的车间名称、编码 不能重复
        Workshop workshopName = workshopDao.getFactoryWorkshopByWorkshopName(params.getFactoryId(), params.getWorkshopName());
        Workshop workshopCode = workshopDao.getFactoryWorkshopByWorkshopCode(params.getFactoryId(), params.getWorkshopCode());
        if (params.getId() > 0) {
            if (workshopName != null && !workshopName.getId().equals(params.getId())) {
                return ResponseVo.ofError("工厂的车间名称不能重复");
            }
            if (workshopCode != null && !workshopCode.getId().equals(params.getId())) {
                return ResponseVo.ofError("工厂的车间名称不能重复");
            }
        } else {
            if (workshopName != null) {
                return ResponseVo.ofError("工厂的车间名称不能重复");
            }
            if (workshopCode != null) {
                return ResponseVo.ofError("工厂的车间名称不能重复");
            }
        }

        //验证工厂信息
        Factory factory = factoryDao.findFactoryByIdAndDeleteStatus(params.getFactoryId(), false);
        if (factory == null) {
            return ResponseVo.ofError("当前车间的工厂信息不存在，请刷新页面重试");
        }
        //联系方式
        if (!ParamsUtils.isTelephone(params.getTelephone())) {
            return ResponseVo.ofError("联系方式格式错误");
        }

        Workshop workshop = new Workshop();
        if (params.getId() > 0) {
            workshop = workshopDao.findWorkshopByIdAndDeleteStatus(params.getId(), false);
            if (workshop == null) {
                return ResponseVo.ofError("车间信息不存在，请刷新页面重试");
            }
            workshop.setEditStatus(1);
        }
        workshop.setCompanyId(factory.getCompanyId());
        workshop.setFactoryId(params.getFactoryId());
        workshop.setWorkshopName(params.getWorkshopName());
        workshop.setWorkshopCode(params.getWorkshopCode());
        workshop.setTelephone(params.getTelephone());
        workshop.setRemark(params.getRemark());
        workshopDao.save(workshop);

        ResponseIdVo responseIdVo = new ResponseIdVo();
        responseIdVo.setId(workshop.getId());
        return ResponseVo.ofSuccess(responseIdVo);
    }

    /**
     * 车间详情
     * @param params
     * @return
     */
    public ResponseVo<WorkshopAddRequestVo> workshopInfoById(WorkshopAddRequestVo params) {
        Workshop workshop = workshopDao.findWorkshopByIdAndDeleteStatus(params.getId(), false);
        if (workshop == null) {
            return ResponseVo.ofError("车间信息不存在，请刷新页面重试");
        }
        WorkshopAddRequestVo responseVo = new WorkshopAddRequestVo();
        BeanUtils.copyProperties(workshop, responseVo);

        return ResponseVo.ofSuccess(responseVo);
    }

    /**
     * 车间删除
     * @param params
     * @return
     */
    public ResponseVo<String> workshopDeleteById(RequestIdVo params) {
        Workshop workshop = workshopDao.findWorkshopByIdAndDeleteStatus(params.getId(), false);
        if (workshop == null) {
            return ResponseVo.ofError("车间信息不存在，请刷新页面重试");
        }
        //工厂下必须保留一个车间
        List<Workshop> workshopList = workshopDao.getAllFactoryWorkshop(workshop.getFactoryId());
        if (workshopList.isEmpty()) {
            return ResponseVo.ofError("车间信息异常，请刷新页面重试");
        }
        if (workshopList.size() == 1) {
            return ResponseVo.ofError("不能删除工厂的最后一个车间");
        }

        //TODO 存在未完成的订单、订单未回款 不可删除 【尚未开发到订单、回款功能】

        //工厂是否存在有效用户
        User user = userDao.getActiveSingleUserByWorkshopId(params.getId());
        if (user != null) {
            return ResponseVo.ofError("当前组织下存在有效或在职员工，无法删除");
        }
        workshop.setWorkshopName(StringUtils.appendRandom(workshop.getWorkshopName()));
        workshop.setWorkshopCode(StringUtils.appendRandom(workshop.getWorkshopCode()));
        workshop.setDeleteStatus(true);
        workshopDao.save(workshop);

        return ResponseVo.ofSuccess();
    }
}
