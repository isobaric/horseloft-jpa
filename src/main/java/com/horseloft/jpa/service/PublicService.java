package com.horseloft.jpa.service;

import com.horseloft.jpa.constant.RoleConstant;
import com.horseloft.jpa.db.dao.CompanyDao;
import com.horseloft.jpa.db.dao.FactoryDao;
import com.horseloft.jpa.db.dao.StationTypeDao;
import com.horseloft.jpa.db.dao.WorkshopDao;
import com.horseloft.jpa.db.entity.*;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.utils.NumberUtils;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.station.StationTypeResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Date: 2020/2/1 下午1:29
 * User: YHC
 * Desc: 公用服务
 */
@Service
public class PublicService {

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private FactoryDao factoryDao;

    @Autowired
    private WorkshopDao workshopDao;

    @Autowired
    private StationTypeDao stationTypeDao;

    /**
     * 验证组织结构的三级信息是否正确
     * @param companyId
     * @param factoryId
     * @param workshopId
     * @return
     */
    public boolean isStructure(@NotNull Long companyId, @NotNull Long factoryId, @NotNull Long workshopId) {
        if (companyId == 0) {
            return false;
        }

        //三个参数都存在
        if (workshopId > 0) {
            Workshop workshop = workshopDao.findWorkshopByIdAndDeleteStatus(workshopId, false);
            return workshop != null && workshop.getFactoryId().equals(factoryId) && workshop.getCompanyId().equals(companyId);
        }

        //只有前两个参数
        if (factoryId > 0) {
            Factory factory = factoryDao.findFactoryByIdAndDeleteStatus(factoryId, false);
            return factory != null && factory.getCompanyId().equals(companyId);
        }

        //只有公司id
        Company company = companyDao.findCompanyByIdAndDeleteStatus(companyId, false);
        return company != null;
    }

    /**
     * 当前用户是否有操作该组织架构的权限|用于新增-编辑功能
     * @param user 当前用户
     * @param companyId
     * @param factoryId
     * @param workshopId
     * @return
     */
    public boolean isHaveStructurePower(User user, Long companyId, Long factoryId, Long workshopId) {
        if (user.getRoleId().contains(RoleConstant.ADMIN_ROLE_ID_STR)) {
            //超级管理员
            return true;
        } else {
            //是否有车间权限
            if (NumberUtils.isActiveLong(workshopId) && this.isHaveWorkshopPower(user, workshopId)) {
                return true;
            }
            //是否有工厂权限
            if (NumberUtils.isActiveLong(factoryId) && this.isHaveFactoryPower(user, factoryId)) {
                return true;
            }
            //是否有公司权限
            return NumberUtils.isActiveLong(companyId) && this.isHaveCompanyPower(user, companyId);
        }
    }

    /**
     * 列表查询中 用户组织结构搜索提条件处理|未筛选组织架构 超级管理员则不使用当前条件
     * @param user
     * @param companyId
     * @param factoryId
     * @param workshopId
     * @return
     */
    public Map<String, Object> getSearchStructure(User user, Long companyId, Long factoryId, Long workshopId) {
        Map<String, Object> map = new HashMap<>();

        //是否有车间权限
        if (NumberUtils.isActiveLong(workshopId) && this.isHaveWorkshopPower(user, workshopId)) {
            map.put("name", "workshopId");
            map.put("value", workshopId);
            return map;
        }
        //是否有工厂权限
        if (NumberUtils.isActiveLong(factoryId) && this.isHaveFactoryPower(user, factoryId)) {
            map.put("name", "factoryId");
            map.put("value", factoryId);
            return map;
        }
        //是否有公司权限
        if (NumberUtils.isActiveLong(companyId) && this.isHaveCompanyPower(user, companyId)) {
            map.put("name", "companyId");
            map.put("value", companyId);
            return map;
        }
        //未筛选组织架构，则使用当前用户的默认组织架构|未筛选组织架构 超级管理员则不使用当前条件
        if (user.getRoleId().contains(RoleConstant.ADMIN_ROLE_ID_STR)) {
            return map;
        } else {
            if (user.getWorkshopId() > 0) {
                map.put("name", "workshopId");
                map.put("value", user.getWorkshopId());
                return map;
            } else if (user.getFactoryId() > 0) {
                map.put("name", "factoryId");
                map.put("value", user.getFactoryId());
                return map;
            } else {
                map.put("name", "companyId");
                map.put("value", user.getCompanyId());
                return map;
            }
        }
    }

    /**
     * 用户是否有车间权限
     * @param user
     * @param workshopId
     * @return
     */
    public boolean isHaveWorkshopPower(User user, @NotNull Long workshopId) {
        if (user.getRoleId().contains(RoleConstant.ADMIN_ROLE_ID_STR)) {
            //超级管理员
            return true;
        } else {
            if (user.getWorkshopId().equals(workshopId)) {
                return true;
            }
            //如果是车间用户，则不能拥有非当前车间的权限
            if (user.getWorkshopId() != 0 && !user.getWorkshopId().equals(workshopId)) {
                return false;
            }
            //允许公司、工厂级别的用户查询，验证用户是否有当前车间的权限
            if (user.getCompanyId() > 0) {
                Workshop workshop = workshopDao.getWorkshopByCompanyIdAndId(user.getCompanyId(), workshopId);
                return workshop != null;
            }
        }
        return false;
    }

    /**
     * 用户是否有工厂权限
     * @param user
     * @param factoryId
     * @return
     */
    public boolean isHaveFactoryPower(User user, @NotNull Long factoryId) {
        if (user.getRoleId().contains(RoleConstant.ADMIN_ROLE_ID_STR)) {
            //超级管理员
            return true;
        } else {
            if (user.getFactoryId().equals(factoryId)) {
                return true;
            }
            //如果是工厂用户，则不能拥有非当前工厂的权限
            if (user.getFactoryId() != 0 && !user.getFactoryId().equals(factoryId)) {
                return false;
            }
            //允许公司级别的用户查询，验证用户是否有当前工厂的权限
            if (user.getCompanyId() > 0) {
                Factory factory = factoryDao.getFactoryByIdAndCompanyId(factoryId, user.getCompanyId());
                return factory != null;
            }
        }
        return false;
    }

    /**
     * 用户是否有公司权限
     * @param user
     * @param companyId
     * @return
     */
    public boolean isHaveCompanyPower(User user, @NotNull Long companyId) {
        if (user.getRoleId().contains(RoleConstant.ADMIN_ROLE_ID_STR)) {
            //超级管理员
            return true;
        } else {
            if (user.getCompanyId().equals(companyId)) {
                return true;
            }
            //如果是公司用户，则不能拥有非当前公司的权限
            if (user.getCompanyId() != 0 && !user.getCompanyId().equals(companyId)) {
                return false;
            }
        }
        return false;
    }

    /**
     * 数据的列表数据 获取组织结构数据
     * @param classList
     * @return
     */
    public Map<String, List<?>> getStructureDataByList(List<?> classList) {
        Map<String, List<?>> map = new HashMap<>();
        Set<Long> companyIds = new HashSet<>();
        Set<Long> factoryIds = new HashSet<>();
        Set<Long> workshopIds = new HashSet<>();

        //获取参数中的公司 工厂 车间id companyId
        classList.forEach(x -> {
            try {
                Class<?> cls = x.getClass();
                Field company = cls.getDeclaredField("companyId");
                company.setAccessible(true);
                Long companyId = (Long) company.get(x);

                Field factory = cls.getDeclaredField("factoryId");
                factory.setAccessible(true);
                Long factoryId = (Long) factory.get(x);

                Field workshop = cls.getDeclaredField("workshopId");
                workshop.setAccessible(true);
                Long workshopId = (Long) workshop.get(x);

                if (companyId > 0) {
                    companyIds.add(companyId);
                }
                if (factoryId > 0) {
                    factoryIds.add(factoryId);
                }
                if (workshopId > 0) {
                    workshopIds.add(workshopId);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(ResponseCode.DATA_ERROR.getMessage());
            }
        });

        //查询公司 工厂 车间数据
        List<Company> companyList = new ArrayList<>();
        if (!companyIds.isEmpty()) {
            companyList = companyDao.findCompaniesByIdIn(new ArrayList<>(companyIds));
        }
        List<Factory> factoryList = new ArrayList<>();
        if (!factoryIds.isEmpty()) {
            factoryList = factoryDao.findFactoriesByIdIn(new ArrayList<>(factoryIds));
        }
        List<Workshop> workshopList = new ArrayList<>();
        if (!workshopIds.isEmpty()) {
            workshopList = workshopDao.findWorkshopsByIdIn(new ArrayList<>(workshopIds));
        }
        map.put("company", companyList);
        map.put("factory", factoryList);
        map.put("workshop", workshopList);
        return map;
    }

    //获取组织结构名称|格式：公司-工厂-车间|listMap数据固定为当前class的 getStructureDataByList() 的返回值
    @SuppressWarnings("unchecked")
    public String getStructureName(Map<String, List<?>> listMap, Long companyId, Long factoryId, Long workshopId) {
        List<Company> companyList = (List<Company>) listMap.get("company");
        List<Factory> factoryList = (List<Factory>) listMap.get("factory");
        List<Workshop> workshopList = (List<Workshop>) listMap.get("workshop");

        StringBuilder stringBuilder = new StringBuilder();
        for(Company company : companyList) {
            if (companyId.equals(company.getId())) {
                stringBuilder.append(company.getComName());
                break;
            }
        }
        for(Factory factory : factoryList) {
            if (factoryId.equals(factory.getId())) {
                stringBuilder.append("-").append(factory.getFactoryName());
                break;
            }
        }
        for(Workshop workshop : workshopList) {
            if (workshopId.equals(workshop.getId())) {
                stringBuilder.append("-").append(workshop.getWorkshopName());
                break;
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 工位类型列表
     * @return
     */
    public ResponseVo<List<StationTypeResponseVo>> getAllStationTypeList() {
        Iterable<StationType> stationType = stationTypeDao.findAll();

        List<StationTypeResponseVo> voList = new ArrayList<>();
        stationType.forEach(x -> {
            StationTypeResponseVo vo = new StationTypeResponseVo();
            BeanUtils.copyProperties(x, vo);
            voList.add(vo);
        });

        return ResponseVo.ofSuccess(voList);
    }
}
