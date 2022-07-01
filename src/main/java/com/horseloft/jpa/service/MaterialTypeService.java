package com.horseloft.jpa.service;

import com.horseloft.jpa.db.dao.MaterialDao;
import com.horseloft.jpa.db.dao.MaterialTypeAttrDao;
import com.horseloft.jpa.db.dao.MaterialTypeAttrValueDao;
import com.horseloft.jpa.db.dao.MaterialTypeDao;
import com.horseloft.jpa.db.entity.Material;
import com.horseloft.jpa.db.entity.MaterialType;
import com.horseloft.jpa.db.entity.MaterialTypeAttr;
import com.horseloft.jpa.db.entity.MaterialTypeAttrValue;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.exception.BusinessException;
import com.horseloft.jpa.utils.StringUtils;
import com.horseloft.jpa.vo.*;
import com.horseloft.jpa.vo.material.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: 2020/2/8 下午2:40
 * User: YHC
 * Desc: 物料类型
 */
@Service
public class MaterialTypeService {

    @Autowired
    private MaterialDao materialDao;

    @Autowired
    private MaterialTypeDao materialTypeDao;

    @Autowired
    private MaterialTypeAttrDao materialTypeAttrDao;

    @Autowired
    private MaterialTypeAttrValueDao materialTypeAttrValueDao;

    /**
     * 物料类型新增-编辑
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<ResponseIdVo> materialTypeAddOrEdit(MaterialTypeAddVo params) {
        MaterialType typeCode = materialTypeDao.findMaterialTypeByTypeCode(params.getTypeCode());
        MaterialType typeName = materialTypeDao.findMaterialTypeByTypeName(params.getTypeName());
        if (params.getId() > 0) {
            if (typeCode != null && !params.getId().equals(typeCode.getId())) {
                return ResponseVo.ofError("编号重复，请重新输入");
            }
            if (typeName != null && !params.getId().equals(typeName.getId())) {
                return ResponseVo.ofError("名称重复，请重新输入");
            }
        } else {
            if (typeCode != null) {
                return ResponseVo.ofError("编号重复，请重新输入");
            }
            if (typeName != null) {
                return ResponseVo.ofError("名称重复，请重新输入");
            }
        }

        //物料类型
        MaterialType materialType = new MaterialType();
        if (params.getId() > 0) {
            materialType = materialTypeDao.findById(params.getId()).orElse(null);
            if (materialType == null) {
                return ResponseVo.ofError("物料类型数据不存在");
            }
        }

        //参数验证：物料类型-属性 | 属性名称不能重复、不能为空 只能新增、不可删除、不可编辑
        if (!params.getAttrList().isEmpty()) {
            Set<String> attrName = new HashSet<>();
            for (MaterialTypeAttrAddVo materialTypeAttrAddVo : params.getAttrList()) {
                //属性名称不能为空
                if (StringUtils.isEmpty(materialTypeAttrAddVo.getAttrName())) {
                    throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "属性名称不能为空");
                }
                attrName.add(materialTypeAttrAddVo.getAttrName());

                //参数验证：物料类型-属性-值 | 属性值名称不能重复、不能为空、不可删除、不可编辑
                Set<String> valueName = new HashSet<>();
                for (MaterialTypeAttrValueVo materialTypeAttrValueVo : materialTypeAttrAddVo.getValueList()) {
                    //属性值名称不能为空
                    if (StringUtils.isEmpty(materialTypeAttrValueVo.getValueName())) {
                        throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "属性值名称不能为空");
                    }
                    valueName.add(materialTypeAttrValueVo.getValueName());
                }
                //属性值名称不能重复
                if (valueName.size() != materialTypeAttrAddVo.getValueList().size()) {
                    throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "属性值名称不能重复");
                }
            }
            //属性名称不能重复
            if (attrName.size() != params.getAttrList().size()) {
                throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "属性名称不能重复");
            }
        }

        materialType.setTypeName(params.getTypeName());
        materialType.setTypeCode(params.getTypeCode());
        materialType.setRemark(params.getRemark());
        materialTypeDao.save(materialType);

        for (MaterialTypeAttrAddVo attrAddVo : params.getAttrList()) {
            long attrId;
            if (attrAddVo.getId() > 0) {
                attrId = attrAddVo.getId();
            } else {
                MaterialTypeAttr attr = new MaterialTypeAttr();
                attr.setTypeId(materialType.getId());
                attr.setAttrName(attrAddVo.getAttrName());
                materialTypeAttrDao.save(attr);
                attrId = attr.getId();
            }
            for (MaterialTypeAttrValueVo valueVo : attrAddVo.getValueList()) {
                if (valueVo.getId() == 0) {
                    MaterialTypeAttrValue value = new MaterialTypeAttrValue();
                    value.setValueName(valueVo.getValueName());
                    value.setTypeId(materialType.getId());
                    value.setAttrId(attrId);
                    materialTypeAttrValueDao.save(value);
                }
            }
        }

        ResponseIdVo vo = new ResponseIdVo();
        vo.setId(materialType.getId());
        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 物料类型详情
     * @param params
     * @return
     */
    public ResponseVo<MaterialTypeAddVo> getMaterialTypeDetailById(RequestIdVo params) {
        MaterialType materialType = materialTypeDao.findById(params.getId()).orElse(null);
        if (materialType == null) {
            return ResponseVo.ofError("物料类型数据不存在，请刷新页面重试");
        }
        //属性-可能为空
        List<MaterialTypeAttr> attrList = materialTypeAttrDao.findMaterialTypeAttrsByTypeId(params.getId());

        //属性值-可能为空
        List<MaterialTypeAttrValue> valueList = new ArrayList<>();
        if (!attrList.isEmpty()) {
            valueList = materialTypeAttrValueDao.findMaterialTypeAttrValuesByTypeId(params.getId());
        }

        List<MaterialTypeAttrAddVo> attrAddVoList = new ArrayList<>();
        for (MaterialTypeAttr attr : attrList) {

            List<MaterialTypeAttrValueVo> valueVoList = new ArrayList<>();
            for (MaterialTypeAttrValue attrValue : valueList) {
                if (attrValue.getAttrId().equals(attr.getId())) {
                    MaterialTypeAttrValueVo valueVo = new MaterialTypeAttrValueVo();
                    valueVo.setId(attrValue.getId());
                    valueVo.setValueName(attrValue.getValueName());
                    valueVoList.add(valueVo);
                }
            }
            MaterialTypeAttrAddVo attrAddVo = new MaterialTypeAttrAddVo();
            attrAddVo.setId(attr.getId());
            attrAddVo.setAttrName(attr.getAttrName());
            attrAddVo.setValueList(valueVoList);

            attrAddVoList.add(attrAddVo);
        }

        MaterialTypeAddVo vo = new MaterialTypeAddVo();
        BeanUtils.copyProperties(materialType, vo);
        vo.setAttrList(attrAddVoList);
        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 物料类型删除
     * @param params
     * @return
     */
    public ResponseVo<String> materialTypeDeleteById(RequestIdVo params) {
        MaterialType materialType = materialTypeDao.findById(params.getId()).orElse(null);
        if (materialType != null) {
            Material material = materialDao.getMaterialByTypeId(params.getId());
            if (material != null) {
                return ResponseVo.ofError("物料类型/属性正在使用，无法删除！");
            }
        }
        return ResponseVo.ofSuccess();
    }

    /**
     * 物料类型列表
     * @return
     */
    public ResponseVo<ResponseListVo<MaterialTypeListVo>> getAllMaterialTypeList() {
        ResponseListVo<MaterialTypeListVo> listVo = new ResponseListVo<>();
        List<MaterialType> materialTypeList = materialTypeDao.getAllMaterialType();
        if (materialTypeList.isEmpty()) {
            return ResponseVo.ofSuccess(listVo);
        }

        Set<Long> ids = new HashSet<>();
        materialTypeList.forEach(x -> ids.add(x.getId()));
        //属性
        List<MaterialTypeAttr> attrList = materialTypeAttrDao.findMaterialsTypeAttrsByTypeIdIn(ids);

        List<MaterialTypeListVo> voList = new ArrayList<>();
        for (MaterialType materialType : materialTypeList) {
            MaterialTypeListVo vo = new MaterialTypeListVo();
            vo.setId(materialType.getId());
            vo.setTypeName(materialType.getTypeName());
            vo.setTypeCode(materialType.getTypeCode());

            StringBuilder stringBuilder = new StringBuilder();
            for (MaterialTypeAttr attr : attrList) {
                if (attr.getTypeId().equals(materialType.getId())) {
                    stringBuilder.append(attr.getAttrName()).append("、");
                }
            }
            if (stringBuilder.length() > 0) {
                vo.setAttrText(StringUtils.trimWithEnd(stringBuilder.toString(), "、"));
            }
            voList.add(vo);
        }
        listVo.setList(voList);

        return ResponseVo.ofSuccess(listVo);
    }

    /**
     * 通用接口-物料类型列表
     * @return
     */
    public ResponseVo<List<MaterialTypeCommonVo>> getCommonMaterialTypeList() {
        Iterable<MaterialType> materialTypes = materialTypeDao.findAll();
        List<MaterialTypeCommonVo> voList = new ArrayList<>();
        for (MaterialType materialType : materialTypes) {
            MaterialTypeCommonVo vo = new MaterialTypeCommonVo();
            vo.setId(materialType.getId());
            vo.setTypeName(materialType.getTypeName());
            voList.add(vo);
        }
        return ResponseVo.ofSuccess(voList);
    }
}
