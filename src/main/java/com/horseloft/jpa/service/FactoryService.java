package com.horseloft.jpa.service;

import com.horseloft.jpa.db.dao.CompanyDao;
import com.horseloft.jpa.db.dao.FactoryDao;
import com.horseloft.jpa.db.dao.UserDao;
import com.horseloft.jpa.db.dao.WorkshopDao;
import com.horseloft.jpa.db.entity.Company;
import com.horseloft.jpa.db.entity.Factory;
import com.horseloft.jpa.db.entity.User;
import com.horseloft.jpa.db.entity.Workshop;
import com.horseloft.jpa.utils.ParamsUtils;
import com.horseloft.jpa.utils.StringUtils;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.structure.FactoryAddRequestVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Date: 2020/1/15 13:03
 * User: YHC
 * Desc: 工厂
 */
@Service
public class FactoryService {

    @Autowired
    private FactoryDao factoryDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private WorkshopDao workshopDao;

    @Autowired
    private UserDao userDao;

    /**
     * 新增编辑工厂
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<ResponseIdVo> factoryAddAndEdit(FactoryAddRequestVo params) {
        //当前公司的工厂名称|编码不能重复
        Factory factoryName = factoryDao.getCompanyFactoryByFactoryName(params.getCompanyId(), params.getFactoryName());
        Factory factoryCode = factoryDao.getCompanyFactoryByFactoryCode(params.getCompanyId(), params.getFactoryCode());
        if (params.getId() > 0) {
            if (factoryName != null && !factoryName.getId().equals(params.getId())) {
                return ResponseVo.ofError("公司的工厂名称不能重复");
            }
            if (factoryCode != null && !factoryCode.getId().equals(params.getId())) {
                return ResponseVo.ofError("公司的工厂编号不能重复");
            }
        } else {
            if (factoryName != null) {
                return ResponseVo.ofError("公司的工厂名称不能重复");
            }
            if (factoryCode != null) {
                return ResponseVo.ofError("公司的工厂编号不能重复");
            }
        }

        //验证公司
        Company company = companyDao.findCompanyByIdAndDeleteStatus(params.getCompanyId(), false);
        if (company == null) {
            return ResponseVo.ofError("当前工厂的公司信息不存在，请刷新页面重试");
        }
        //联系方式
        if (!ParamsUtils.isTelephone(params.getTelephone())) {
            return ResponseVo.ofError("联系方式格式错误");
        }

        Factory factoryEdit = new Factory();
        //受用save方法更新
        if (params.getId() > 0) {
            factoryEdit = factoryDao.findFactoryByIdAndDeleteStatus(params.getId(), false);
            if (factoryEdit == null) {
                return ResponseVo.ofError("工厂信息不存在，请刷新页面重试");
            }
            factoryEdit.setEditStatus(1);
        } else {
            factoryEdit.setCompanyId(params.getCompanyId());
            factoryEdit.setFactoryName(params.getFactoryName());
            factoryEdit.setAddress(params.getAddress());
            factoryEdit.setEditStatus(1);
            factoryEdit.setFactoryCode(params.getFactoryCode());
            factoryEdit.setTelephone(params.getTelephone());
            factoryDao.save(factoryEdit);
        }

        //新增工厂 同步新增车间
        if (params.getId() == 0) {
            Workshop workshop = new Workshop();
            workshop.setCompanyId(params.getCompanyId());
            workshop.setFactoryId(factoryEdit.getId());
            workshop.setWorkshopName("车间（未编辑）");
            workshopDao.save(workshop);
        }

        ResponseIdVo responseIdVo = new ResponseIdVo();
        responseIdVo.setId(factoryEdit.getId());
        return ResponseVo.ofSuccess();
    }

    /**
     * 工厂详情
     * @param params
     * @return
     */
    public ResponseVo<FactoryAddRequestVo> factoryInfoById(RequestIdVo params) {
        Factory factory = factoryDao.findFactoryByIdAndDeleteStatus(params.getId(), false);
        if (factory == null) {
            return ResponseVo.ofError("工厂信息不存在，请刷新页面重试");
        }
        FactoryAddRequestVo responseVo = new FactoryAddRequestVo();
        BeanUtils.copyProperties(factory, responseVo);

        return ResponseVo.ofSuccess(responseVo);
    }

    /**
     * 工厂删除
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<String> factoryDeleteById(RequestIdVo params) {
        Factory factory = factoryDao.findFactoryByIdAndDeleteStatus(params.getId(), false);
        if (factory == null) {
            return ResponseVo.ofError("工厂信息不存在，请刷新页面重试");
        }
        List<Factory> factoryList = factoryDao.getAllCompanyFactory(params.getId());
        if (factoryList.isEmpty()) {
            return ResponseVo.ofError("工厂信息异常，请刷新页面重试");
        }
        if (factoryList.size() <= 1) {
            return ResponseVo.ofError("不能删除公司的最后一个工厂");
        }

        //TODO 存在未完成的订单、订单未回款 不可删除 【尚未开发到订单、回款功能】

        //存在有效员工不可删除
        User user = userDao.getActiveSingleUserByFactoryId(params.getId());
        if (user != null) {
            return ResponseVo.ofError("当前组织下存在有效或在职员工，无法删除");
        }
        factory.setFactoryName(StringUtils.appendRandom(factory.getFactoryName()));
        factory.setFactoryCode(StringUtils.appendRandom(factory.getFactoryCode()));
        factory.setDeleteStatus(true);
        factoryDao.save(factory);

        //删除工厂下的车间
        List<Workshop> workshopList = workshopDao.getAllCompanyWorkshop(factory.getCompanyId());
        for (Workshop workshop : workshopList) {
            workshop.setWorkshopName(StringUtils.appendRandom(workshop.getWorkshopName()));
            workshop.setWorkshopCode(StringUtils.appendRandom(workshop.getWorkshopCode()));
            workshop.setDeleteStatus(true);
            workshopDao.save(workshop);
        }

        return ResponseVo.ofSuccess();
    }

}
