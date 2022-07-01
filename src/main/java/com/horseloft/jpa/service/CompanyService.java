package com.horseloft.jpa.service;

import com.horseloft.jpa.db.dao.*;
import com.horseloft.jpa.db.entity.*;
import com.horseloft.jpa.utils.ParamsUtils;
import com.horseloft.jpa.utils.StringUtils;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.RequestVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.structure.CompanyAddRequestVo;
import com.horseloft.jpa.vo.structure.FactoryListResponseVo;
import com.horseloft.jpa.vo.structure.StructureListResponseVo;
import com.horseloft.jpa.vo.structure.WorkshopListResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Date: 2020/1/7 16:15
 * User: YHC
 * Desc: 工厂
 */
@Service
public class CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private DistrictDao districtDao;

    @Autowired
    private FactoryDao factoryDao;

    @Autowired
    private WorkshopDao workshopDao;

    @Autowired
    private UserDao userDao;

    /**
     * 新增工厂|同时创建工厂三级结构|删除则在名称和编号后面+字符
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<ResponseIdVo> companyBuild(CompanyAddRequestVo params) {
        //公司名称 编号 不重复
        Company nameData = companyDao.getBaseInfoByComName(params.getComName());
        Company codeData = companyDao.getBaseInfoByComCode(params.getComCode());
        if (params.getId() != null && params.getId() > 0) {
            if (nameData != null && !params.getId().equals(nameData.getId())) {
                return ResponseVo.ofError("公司名已存在");
            }
            if (codeData != null && !params.getId().equals(codeData.getId())) {
                return ResponseVo.ofError("公司编码已存在");
            }
        } else {
            if (nameData != null) {
                return ResponseVo.ofError("公司名已存在");
            }
            if (codeData != null) {
                return ResponseVo.ofError("公司编码已存在");
            }
        }
        //简称
        if (StringUtils.isBlank(params.getShortName()) || params.getShortName().length() > 6) {
            return ResponseVo.ofError("公司简称格式错误");
        }
        //联系方式 手机号、座机号
        if (!StringUtils.isBlank(params.getTelephone()) && !ParamsUtils.isTelephone(params.getTelephone())) {
            return ResponseVo.ofError("联系方式格式错误");
        }
        //法人
        if (!StringUtils.isBlank(params.getTelephone()) && !ParamsUtils.isPeopleName(params.getLegalPerson())) {
            return ResponseVo.ofError("公司法人格式错误");
        }
        //地址
        if (!StringUtils.isBlank(params.getAddress()) && (params.getAddress().length() < 6 || params.getAddress().length() > 60)) {
            return ResponseVo.ofError("公司地址格式错误");
        }
        //网址
        if (!StringUtils.isBlank(params.getWebsite()) && !ParamsUtils.isUrl(params.getWebsite())) {
            return ResponseVo.ofError("公司网址格式错误");
        }
        //规模
        if (params.getEmployeeNumber() > 100000) {
            return ResponseVo.ofError("公司规模格式错误");
        }
        //行业
        if (!StringUtils.isBlank(params.getIndustry())
                && !ParamsUtils.isChineseName(params.getIndustry())
                && (params.getIndustry().length() > 20 || params.getIndustry().length() < 2)
        ) {
            return ResponseVo.ofError("行业格式错误");
        }
        //从低级验证高级
        District district;
        boolean isArea = false;
        boolean isCity = false;
        //区县
        if (params.getAreaId() > 0) {
            isArea = true;
            isCity = true;
            district = districtDao.getBaseInfoById(params.getAreaId(), 3);
            if (district == null) {
                return ResponseVo.ofError("地区的区县错误");
            }
            //获取城市 省份
            District districtCity = districtDao.getBaseInfoById(district.getPid(), 2);
            if (districtCity == null) {
                return ResponseVo.ofError("地区的市错误");
            }
            params.setProvinceId(districtCity.getPid());
            params.setCityId(districtCity.getId());
        }
        //城市
        if (params.getCityId() > 0 && !isArea) {
            isCity = true;
            district = districtDao.getBaseInfoById(params.getAreaId(), 2);
            if (district == null) {
                return ResponseVo.ofError("区县错误");
            }
            params.setProvinceId(district.getPid());
        }
        //省份
        if (params.getProvinceId() > 0 && !isCity) {
            district = districtDao.getBaseInfoById(params.getProvinceId(), 1);
            if (district == null) {
                return ResponseVo.ofError("省份错误");
            }
        }
        //备注
        if (!StringUtils.isBlank(params.getRemark()) && params.getRemark().length() > 150) {
            return ResponseVo.ofError("备注已超过150个字符");
        }

        Company companyBuild = new Company();
        if (params.getId() > 0) {
            companyBuild = companyDao.findCompanyByIdAndDeleteStatus(params.getId(), false);
            if (companyBuild == null) {
                return ResponseVo.ofError("您编辑的公司信息不存在");
            }
        }
        companyBuild.setDeleteStatus(false);
        companyBuild.setComName(params.getComName());
        companyBuild.setComCode(params.getComCode());
        companyBuild.setTelephone(params.getTelephone());
        companyBuild.setShortName(params.getShortName());
        companyBuild.setLegalPerson(params.getLegalPerson());
        companyBuild.setEmail(params.getEmail());
        companyBuild.setAddress(params.getAddress());
        companyBuild.setWebsite(params.getWebsite());
        companyBuild.setEmployeeNumber(params.getEmployeeNumber());
        companyBuild.setIndustry(params.getIndustry());
        companyBuild.setProvinceId(params.getProvinceId());
        companyBuild.setCityId(params.getCityId());
        companyBuild.setAreaId(params.getAreaId());
        companyBuild.setRemark(params.getRemark());
        companyDao.save(companyBuild);

        if (params.getId() == 0) {
            //新增工厂
            Factory factory = new Factory();
            factory.setFactoryName("工厂（未编辑）");
            factory.setCompanyId(companyBuild.getId());
            factoryDao.save(factory);

            //新增车间
            Workshop workshop = new Workshop();
            workshop.setCompanyId(companyBuild.getId());
            workshop.setFactoryId(factory.getId());
            workshop.setWorkshopName("车间（未编辑）");
            workshopDao.save(workshop);
        }

        ResponseIdVo responseIdVo = new ResponseIdVo();
        responseIdVo.setId(companyBuild.getId());
        return ResponseVo.ofSuccess(responseIdVo);
    }

    /**
     * 组织结构列表
     * @param params
     * @return
     */
    public ResponseVo<List<StructureListResponseVo>> structureList(RequestVo params) {
        List<StructureListResponseVo> listResponseVos = new ArrayList<>();
        //全部公司
        List<Company> companyList = companyDao.getAllCompany();
        if (companyList.isEmpty()) {
            return ResponseVo.ofSuccess(listResponseVos);
        }
        //全部工厂
        List<Factory> factoryList = factoryDao.getAllFactory();
        //全部车间
        List<Workshop> workshopList = workshopDao.getAllWorkshop();

        Set<Long> companyIdSet = new HashSet<>();
        Set<Long> factoryIdSet = new HashSet<>();
        Set<Long> workshopIdSet = new HashSet<>();
        companyList.forEach(x -> companyIdSet.add(x.getId()));

        //是否为最后一个工厂
        int isCompanyFinally = companyList.size() > 1 ? 0: 1;

        //是否为公司的最后一个工厂
        Map<Long, Integer> factoryFinallyMap = new HashMap<>();
        factoryList.forEach(x -> {
            factoryIdSet.add(x.getId());
            factoryFinallyMap.merge(x.getCompanyId(), 1, Integer::sum);
        });

        //是否为工厂的最后一个车间
        Map<Long, Integer> workshopFinallyMap = new HashMap<>();
        workshopList.forEach(x -> {
            workshopIdSet.add(x.getId());
            workshopFinallyMap.merge(x.getFactoryId(), 1, Integer::sum);
        });

        //TODO 存在未完成的订单、订单未回款 不可删除

        //存在有效员工不可删除||车间存在有效员工 不可删除车间、工厂、公司|工厂存在有效员工 不可删除工厂、公司|公司存在有效员工 不可删除工厂
        Set<Long> enableWorkshopSet = new HashSet<>();
        Set<Long> enableFactorySet = new HashSet<>();
        Set<Long> enableCompanySet = new HashSet<>();
        //车间员工
        if (!workshopIdSet.isEmpty()) {
            List<User> workshopUserList = userDao.getActiveSingleUserByWorkshopIds(new ArrayList<>(workshopIdSet));
            workshopUserList.forEach(x -> {
                enableCompanySet.add(x.getCompanyId());
                enableFactorySet.add(x.getFactoryId());
                enableWorkshopSet.add(x.getWorkshopId());
            });
        }
        //工厂员工
        if (!factoryIdSet.isEmpty()) {
            List<User> factoryUserList = userDao.getActiveSingleUserByFactoryIds(new ArrayList<>(factoryIdSet));
            factoryUserList.forEach(x -> {
                enableFactorySet.add(x.getFactoryId());
                enableCompanySet.add(x.getCompanyId());
            });
        }
        //公司员工
        List<User> CompanyUserList = userDao.getActiveSingleUserByCompanyIds(new ArrayList<>(companyIdSet));
        CompanyUserList.forEach(x -> enableCompanySet.add(x.getCompanyId()));

        for(Company company : companyList) {
            StructureListResponseVo StructureListResponseVo = new StructureListResponseVo();
            StructureListResponseVo.setComName(company.getComName());
            StructureListResponseVo.setCompanyId(company.getId());
            StructureListResponseVo.setTelephone(company.getTelephone());
            StructureListResponseVo.setFinallyStatus(isCompanyFinally);
            if (enableCompanySet.contains(company.getId())) {
                StructureListResponseVo.setRemoveStatus(0);
            } else {
                StructureListResponseVo.setRemoveStatus(1);
            }
            //第二层|工厂
            List<FactoryListResponseVo> twoRank = new ArrayList<>();
            for(Factory factory : factoryList) {
                if (company.getId().equals(factory.getCompanyId())) {
                    FactoryListResponseVo factoryListResponseVo = new FactoryListResponseVo();
                    factoryListResponseVo.setFactoryId(factory.getId());
                    factoryListResponseVo.setFactoryName(factory.getFactoryName());
                    if (factory.getEditStatus().equals(1)) {
                        factoryListResponseVo.setTelephone(factory.getTelephone());
                        factoryListResponseVo.setAddress(factory.getAddress());
                    } else {
                        factoryListResponseVo.setTelephone("未编辑");
                        factoryListResponseVo.setAddress("未编辑");
                    }
                    if (factoryFinallyMap.get(factory.getCompanyId()) != null && factoryFinallyMap.get(factory.getCompanyId()) > 1) {
                        factoryListResponseVo.setFinallyStatus(0);
                    } else {
                        factoryListResponseVo.setFinallyStatus(1);
                    }
                    if (enableFactorySet.contains(factory.getId())) {
                        factoryListResponseVo.setRemoveStatus(0);
                    } else {
                        factoryListResponseVo.setRemoveStatus(1);
                    }
                    //第三层|车间
                    List<WorkshopListResponseVo> threeRank = new ArrayList<>();
                    for(Workshop workshop : workshopList) {
                        if (workshop.getFactoryId().equals(factory.getId())) {
                            WorkshopListResponseVo workshopListResponseVo = new WorkshopListResponseVo();
                            workshopListResponseVo.setWorkshopName(workshop.getWorkshopName());
                            workshopListResponseVo.setWorkshopId(workshop.getId());
                            if (workshop.getEditStatus() == 1) {
                                workshopListResponseVo.setTelephone(workshop.getTelephone());
                            } else {
                                workshopListResponseVo.setTelephone("未编辑");
                            }
                            if (workshopFinallyMap.get(workshop.getFactoryId()) != null && workshopFinallyMap.get(workshop.getFactoryId()) > 1) {
                                workshopListResponseVo.setFinallyStatus(0);
                            } else {
                                workshopListResponseVo.setFinallyStatus(1);
                            }
                            if (enableWorkshopSet.contains(workshop.getId())) {
                                workshopListResponseVo.setRemoveStatus(0);
                            } else {
                                workshopListResponseVo.setRemoveStatus(1);
                            }
                            threeRank.add(workshopListResponseVo);
                        }
                    }
                    factoryListResponseVo.setWorkshopList(threeRank);
                    twoRank.add(factoryListResponseVo);
                }
            }
            StructureListResponseVo.setFactoryList(twoRank);
            listResponseVos.add(StructureListResponseVo);
        }

        return ResponseVo.ofSuccess(listResponseVos);
    }

    /**
     * 公司信息详情
     * @param params
     * @return
     */
    public ResponseVo<CompanyAddRequestVo> companyBaseInfoById(RequestIdVo params) {

        Company company = companyDao.findCompanyByIdAndDeleteStatus(params.getId(), false);
        if (company == null) {
            return ResponseVo.ofError("当前公司信息不存在");
        }
        CompanyAddRequestVo responseVo = new CompanyAddRequestVo();
        BeanUtils.copyProperties(company, responseVo);

        return ResponseVo.ofSuccess(responseVo);
    }

    /**
     * 公司删除|同时删除公司下的全部工厂和车间
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<String> companyDeleteById(RequestIdVo params) {
        //是否为最后一个公司
        List<Company> companyList = companyDao.getAllCompany();
        if (companyList.isEmpty()) {
            return ResponseVo.ofError("公司信息不存在");
        }
        if (companyList.size() == 1) {
            return ResponseVo.ofError("不能删除最后一个公司");
        }
        Company company = companyDao.findCompanyByIdAndDeleteStatus(params.getId(), false);
        if (company == null) {
            return ResponseVo.ofError("您要删除的公司信息不存在");
        }

        //TODO 存在未完成的订单、订单未回款 不可删除 

        //存在有效员工不可删除||车间存在有效员工 不可删除车间、工厂、公司|工厂存在有效员工 不可删除工厂、公司|公司存在有效员工 不可删除工厂
        User user = userDao.getActiveSingleUserByCompanyId(params.getId());
        if (user != null) {
            return ResponseVo.ofError("当前组织下存在有效或在职员工，无法删除");
        }

        //删除公司
        company.setComName(StringUtils.appendRandom(company.getComName()));
        company.setComCode(StringUtils.appendRandom(company.getComCode()));
        company.setDeleteStatus(true);
        companyDao.save(company);

        //删除工厂
        List<Factory> factoryList = factoryDao.getAllCompanyFactory(params.getId());
        for (Factory factory : factoryList) {
            factory.setFactoryName(StringUtils.appendRandom(factory.getFactoryName()));
            factory.setFactoryCode(StringUtils.appendRandom(factory.getFactoryCode()));
            factory.setDeleteStatus(true);
        }
        if (!factoryList.isEmpty()) {
            factoryDao.saveAll(factoryList);
        }

        //删除车间
        List<Workshop> workshopList = workshopDao.getAllCompanyWorkshop(params.getId());
        for (Workshop workshop : workshopList) {
            workshop.setWorkshopName(StringUtils.appendRandom(workshop.getWorkshopName()));
            workshop.setWorkshopCode(StringUtils.appendRandom(workshop.getWorkshopCode()));
            workshop.setDeleteStatus(true);
        }
        if (!workshopList.isEmpty()) {
            workshopDao.saveAll(workshopList);
        }

        return ResponseVo.ofSuccess();
    }
}
