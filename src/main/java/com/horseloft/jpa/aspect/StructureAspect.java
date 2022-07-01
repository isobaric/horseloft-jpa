package com.horseloft.jpa.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.horseloft.jpa.annotation.Structure;
import com.horseloft.jpa.db.dao.DeviceDao;
import com.horseloft.jpa.db.dao.ShelfDao;
import com.horseloft.jpa.db.dao.StationDao;
import com.horseloft.jpa.db.dao.UserDao;
import com.horseloft.jpa.db.entity.User;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.exception.BusinessException;
import com.horseloft.jpa.service.PublicService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Date: 2020/2/3 下午3:39
 * User: YHC
 * Desc: 不用于文件上传、下载
 *       仅用于验证当前用户是否有权限操作请求参数中的组织结构
 *       并验证参数中的组织结构是否正确
 */
@Slf4j
@Aspect
@Component
public class StructureAspect {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private ShelfDao shelfDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PublicService publicService;

    @Pointcut("@annotation(com.horseloft.jpa.annotation.Structure)")
    public void structurePointCut() {
    }

    @Before("structurePointCut()")
    public void doBefore(JoinPoint joinPoint) {
        //请求参数
        Object[] args = joinPoint.getArgs();
        JSONArray jsonParams = JSON.parseArray(JSON.toJSONString(args));
        if (jsonParams.isEmpty() || !(jsonParams.get(0) instanceof JSONObject)) {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "请求参数格式错误");
        }

        //注解及值
        MethodSignature joinPointSignature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPointSignature.getMethod();
        Structure structure = method.getAnnotation(Structure.class);
        String daoName = structure.value().toString();

        if ("".equals(daoName)) {
            //新增编辑接口使用
            this.requestAddOrEditHandle(jsonParams);
        } else {
            //请求参数为id的接口使用
            this.requestIdHandle(daoName, jsonParams);
        }
    }

    //编辑-添加功能验证
    private void requestAddOrEditHandle(JSONArray jsonParams) {
        Long companyId = ((JSONObject) jsonParams.get(0)).getLong("companyId");
        Long factoryId = ((JSONObject) jsonParams.get(0)).getLong("factoryId");
        Long workshopId = ((JSONObject) jsonParams.get(0)).getLong("workshopId");
        User user = JSON.parseObject( ((JSONObject) jsonParams.get(0)).get("user").toString(), User.class);
        if (companyId == null || factoryId == null || workshopId == null || user == null) {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR);
        }

        //验证是否有参数中的组织结构的权限
        if (!publicService.isHaveStructurePower(user, companyId, factoryId, workshopId)) {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "您没有权限添加/编辑当前组织结构的数据");
        }

        //验证组织结构是否正确
        if (!publicService.isStructure(companyId, factoryId, workshopId)) {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "组织架构错误，请刷新页面重试");
        }
    }

    //参数只有id的 查询 删除等操作的验证
    private void requestIdHandle (String daoName, JSONArray jsonParams) {
        Long id = ((JSONObject) jsonParams.get(0)).getLong("id");
        if (id == null) {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "参数ID不存在");
        }

        //bean不存在抛出异常 |
        CrudRepository<?, Long> crud = this.getDaoByName(daoName);
        Object data = crud.findById(id).orElse(null);
        if (data == null) {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "组织架构数据不存在");
        }

        Long companyId;
        Long factoryId;
        Long workshopId;
        User user = JSON.parseObject( ((JSONObject) jsonParams.get(0)).get("user").toString(), User.class);

        try {
            Class<?> cls = data.getClass();

            Field company = cls.getDeclaredField("companyId");
            company.setAccessible(true);
            companyId = (Long) company.get(data);

            Field factory = cls.getDeclaredField("factoryId");
            factory.setAccessible(true);
            factoryId = (Long) factory.get(data);

            Field workshop = cls.getDeclaredField("workshopId");
            workshop.setAccessible(true);
            workshopId = (Long) workshop.get(data);

        } catch (Exception e) {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "组织架构数据错误");
        }
        if (companyId == null || factoryId == null || workshopId == null || user == null) {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR);
        }

        //验证当前用户是否有组织结构的权限
        if (!publicService.isHaveStructurePower(user, companyId, factoryId, workshopId)) {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "您没有权限操作当前组织架构的数据");
        }
    }

    /**
     * 获取指定的Dao
     * @param daoName
     * @return
     */
    private CrudRepository<?, Long> getDaoByName(String daoName) {
        if ("userDao".equals(daoName)) {
            return userDao;
        }

        if ("shelfDao".equals(daoName)) {
            return shelfDao;
        }

        if ("deviceDao".equals(daoName)) {
            return deviceDao;
        }

        if ("stationDao".equals(daoName)) {
            return stationDao;
        }
        throw new BusinessException(ResponseCode.SERVER_ERROR);
    }
}
