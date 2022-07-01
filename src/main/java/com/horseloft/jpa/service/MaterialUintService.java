package com.horseloft.jpa.service;

import com.horseloft.jpa.db.dao.MaterialDao;
import com.horseloft.jpa.db.dao.MaterialUnitDao;
import com.horseloft.jpa.db.entity.Material;
import com.horseloft.jpa.db.entity.MaterialUnit;
import com.horseloft.jpa.enums.MaterialUnitType;
import com.horseloft.jpa.utils.StringUtils;
import com.horseloft.jpa.vo.*;
import com.horseloft.jpa.vo.material.MaterialUnitAddVo;
import com.horseloft.jpa.vo.material.MaterialUnitCommonVo;
import com.horseloft.jpa.vo.material.MaterialUnitListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2020/2/9 上午10:47
 * User: YHC
 * Desc: 物料主单位
 */
@Service
public class MaterialUintService {

    @Autowired
    private MaterialDao materialDao;

    @Autowired
    private MaterialUnitDao materialUnitDao;

    /**
     * 物料单位添加-编辑
     * @param params
     * @return
     */
    public ResponseVo<ResponseIdVo> materialUnitBuild(MaterialUnitAddVo params) {
        MaterialUnit unitCode = materialUnitDao.findMaterialUnitByUnitCode(params.getUnitCode());
        MaterialUnit unitName = materialUnitDao.findMaterialUnitByUnitName(params.getUnitName());
        if (params.getId() > 0) {
            if (unitCode != null && !unitCode.getId().equals(params.getId())) {
                return ResponseVo.ofError("计量单位重复");
            }
            if (unitName != null && !unitName.getId().equals(params.getId())) {
                return ResponseVo.ofError("单位名称重复");
            }
        } else {
            if (unitCode != null) {
                return ResponseVo.ofError("计量单位重复");
            }
            if (unitName != null) {
                return ResponseVo.ofError("单位名称重复");
            }
        }

        MaterialUnit unit = new MaterialUnit();
        if (params.getId() > 0) {
            unit = materialUnitDao.findById(params.getId()).orElse(null);
            if (unit == null) {
                return ResponseVo.ofError("您编辑的数据不存在，请刷新页面重试");
            }
        }
        unit.setType(params.getType());
        unit.setUnitCode(params.getUnitCode());
        unit.setUnitName(params.getUnitName());
        materialUnitDao.save(unit);

        ResponseIdVo vo = new ResponseIdVo();
        vo.setId(unit.getId());
        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 物料单位详情
     * @param params
     * @return
     */
    public ResponseVo<MaterialUnitAddVo> getMaterialUnitDetailById(RequestIdVo params) {
        MaterialUnit unit = materialUnitDao.findById(params.getId()).orElse(null);
        if (unit == null || unit.getDeleteStatus()) {
            return ResponseVo.ofError("物料主单位数据不存在");
        }
        MaterialUnitAddVo vo = new MaterialUnitAddVo();
        BeanUtils.copyProperties(unit, vo);
        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 物料单位删除
     * @param params
     * @return
     */
    public ResponseVo<String> materialUnitDeleteById(RequestIdVo params) {
        MaterialUnit unit = materialUnitDao.findById(params.getId()).orElse(null);
        if (unit != null && unit.getDeleteStatus()) {
            Material material = materialDao.getMaterialByUnitId(params.getId());
            if (material != null) {
                return ResponseVo.ofError("物料主单位正在使用 无法删除！");
            }
            unit.setDeleteStatus(true);
            unit.setUnitName(StringUtils.appendRandom(unit.getUnitName()));
            unit.setUnitCode(StringUtils.appendRandom(unit.getUnitCode()));
            materialUnitDao.save(unit);
        }
        return ResponseVo.ofSuccess();
    }

    /**
     * 物料单位列表
     * @return
     */
    public ResponseVo<ResponseListVo<MaterialUnitListVo>> getMaterialUnitList() {
        ResponseListVo<MaterialUnitListVo> listVo = new ResponseListVo<>();
        List<MaterialUnit> unitList = materialUnitDao.findAllByOrderByIdDesc();

        List<MaterialUnitListVo> voList = new ArrayList<>();
        for (MaterialUnit unit : unitList) {
            MaterialUnitListVo vo = new MaterialUnitListVo();
            BeanUtils.copyProperties(unit, vo);
            vo.setTypeText(MaterialUnitType.getName(unit.getType()));
            voList.add(vo);
        }
        listVo.setList(voList);

        return ResponseVo.ofSuccess(listVo);
    }

    /**
     * 通用-物料单位列表
     * @return
     */
    public ResponseVo<List<MaterialUnitCommonVo>> getCommonMaterialUnitList() {
        List<MaterialUnitCommonVo> list = new ArrayList<>();
        List<MaterialUnit> unitList = materialUnitDao.findAllByOrderByIdAsc();

        for (MaterialUnit unit : unitList) {
            MaterialUnitCommonVo vo = new MaterialUnitCommonVo();
            vo.setId(unit.getId());
            vo.setUnitName(unit.getUnitName());
            list.add(vo);
        }

        return ResponseVo.ofSuccess(list);
    }
}
