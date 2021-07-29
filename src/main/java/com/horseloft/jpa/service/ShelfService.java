package com.horseloft.jpa.service;

import com.horseloft.jpa.constant.RoleConstant;
import com.horseloft.jpa.db.dao.*;
import com.horseloft.jpa.db.entity.*;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.exception.BusinessException;
import com.horseloft.jpa.utils.FileUtils;
import com.horseloft.jpa.utils.NumberUtils;
import com.horseloft.jpa.utils.StringUtils;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseListVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.shelf.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: 2020/2/5 下午1:55
 * User: YHC
 * Desc: 货架
 */
@Service
public class ShelfService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SlotDao slotDao;

    @Autowired
    private ShelfDao shelfDao;

    @Autowired
    private LatticeDao latticeDao;

    @Autowired
    private MaterialDao materialDao;

    /**
     * 新增货架
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<ResponseIdVo> shelfAddByCurrentUser(ShelfAddRequestVo params) {
        //同步添加货箱 货位
        Shelf info = shelfDao.findShelfByShelfCode(params.getShelfCode());
        if (info != null) {
            return ResponseVo.ofError("货架编号已存在");
        }
        Shelf shelf = new Shelf();
        shelf.setCompanyId(params.getCompanyId());
        shelf.setFactoryId(params.getFactoryId());
        shelf.setWorkshopId(params.getWorkshopId());
        shelf.setShelfCode(params.getShelfCode());
        shelfDao.save(shelf);

        //添加两个货位 并为货位添加货箱
        for (int i = 0; i < 2; i++) {
            Slot slot = new Slot();
            Lattice lattice = new Lattice();
            if (i == 0) {
                slot.setSlotCode("HX-" + params.getShelfCode() + "-R");
                lattice.setLatticeCode(params.getShelfCode() + "-R");
            } else {
                slot.setSlotCode("HX-" + params.getShelfCode() + "-L");
                lattice.setLatticeCode(params.getShelfCode() + "-L");
            }
            lattice.setShelfId(shelf.getId());
            latticeDao.save(lattice);

            //添加货箱
            slot.setShelfId(shelf.getId());
            slot.setLatticeId(lattice.getId());
            slotDao.save(slot);
        }

        ResponseIdVo vo = new ResponseIdVo();
        vo.setId(shelf.getId());
        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 货架详情
     * @param params
     * @return
     */
    public ResponseVo<ShelfDetailResponseVo> getShelfDetailById(RequestIdVo params) {
        Shelf shelf = shelfDao.findById(params.getId()).orElse(null);
        if (shelf == null) {
            return ResponseVo.ofError("货架信息不存在");
        }
        ShelfDetailResponseVo responseVo = new ShelfDetailResponseVo();
        BeanUtils.copyProperties(shelf, responseVo);

        //货箱
        List<Slot> slotList = slotDao.findSlotsByShelfId(params.getId());
        if (slotList.isEmpty()) {
            return ResponseVo.ofSuccess(responseVo);
        }

        List<User> userList = new ArrayList<>();
        List<Material> materialList = new ArrayList<>();
        Set<Long> materialIds = new HashSet<>();
        Set<Long> userIds = new HashSet<>();
        slotList.forEach(x -> {
            if (x.getMaterialId() > 0) {
                materialIds.add(x.getMaterialId());
            }
            if (x.getHandleUserId() > 0) {
                userIds.add(x.getHandleUserId());
            }
            if (x.getReportUserId() > 0) {
                userIds.add(x.getReportUserId());
            }
        });

        //物料
        if (!materialIds.isEmpty()) {
            materialList = materialDao.findMaterialsByIdIn(materialIds);
        }
        //用户
        if (!userIds.isEmpty()) {
            userList = userDao.findUsersByIdIn(userIds);
        }

        List<ShelfSlotVo> slotVoList = new ArrayList<>();
        for (Slot slot : slotList) {
            ShelfSlotVo slotVo = new ShelfSlotVo();
            BeanUtils.copyProperties(slot, slotVo);

            for (Material material : materialList) {
                if (slot.getMaterialId().equals(material.getId())) {
                    slotVo.setMaterialCode(material.getMaterialCode());
                    slotVo.setMaterialName(material.getMaterialName());
                    break;
                }
            }
            for (User user : userList) {
                if (slot.getHandleUserId().equals(user.getId())) {
                    slotVo.setHandleUserName(user.getRealName());
                }
                if (slot.getReportUserId().equals(user.getId())) {
                    slotVo.setReportUserName(user.getRealName());
                }
            }
            slotVoList.add(slotVo);
        }
        responseVo.setList(slotVoList);

        return ResponseVo.ofSuccess(responseVo);
    }

    /**
     * 货架删除
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<String> deleteShelfById(RequestIdVo params) {
        //同时删除 货箱 货位
        Shelf shelf = shelfDao.findById(params.getId()).orElse(null);
        if (shelf != null) {
            if (shelf.getMapStatus()) {
                return ResponseVo.ofError("货架正在使用，无法删除！");
            }
            shelfDao.deleteById(params.getId());
            slotDao.deleteSlotsByShelfId(params.getId());
            latticeDao.deleteLatticesByShelfId(params.getId());
        }

        return ResponseVo.ofSuccess();
    }

    /**
     * 货架列表导出
     * @param params
     * @param response
     * @throws Exception
     */
    public void shelfListDownload(ShelfListRequestVo params, HttpServletResponse response) throws Exception {
        Specification<Shelf> specification = this.shelfSpecification(params);
        if (specification instanceof Specification) {
            Sort sort = Sort.by(Sort.Order.asc("id"));
            List<Shelf> shelfPage = shelfDao.findAll(specification, sort);
            if (shelfPage.isEmpty()) {
                throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "您下载的数据为空");
            }
            FileUtils.excelDownload(response, this.shelfListDownloadMixer(shelfPage), "货架列表");
        } else {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "您下载的数据为空");
        }
    }

    /**
     * 货架列表
     * @param params
     * @return
     */
    public ResponseVo<ResponseListVo<ShelfListResponseVo>> getShelfListByCurrentUser(ShelfListRequestVo params) {
        ResponseListVo<ShelfListResponseVo> listVo = new ResponseListVo<>();
        listVo.setPage(params.getPage());
        listVo.setPageSize(params.getPageSize());
        Specification<Shelf> specification = this.shelfSpecification(params);
        if (specification instanceof Specification) {
            Sort sort = Sort.by(Sort.Order.asc("id"));
            Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), sort);
            Page<Shelf> shelfPage = shelfDao.findAll(specification, pageable);
            listVo.setTotal(shelfPage.getTotalElements());
            listVo.setTotalPage(shelfPage.getTotalPages());
            listVo.setList(this.shelfListMixer(shelfPage.getContent()));
        }
        return ResponseVo.ofSuccess(listVo);
    }

    //货架查询条件
    private Specification<Shelf> shelfSpecification(ShelfListRequestVo params) {
        String shelfCode = null;
        Set<Long> shelfIds = new HashSet<>();
        List<Slot> slotList = new ArrayList<>();

        //搜索框|2物料编号，3生产订单编号，4物料名称 需要先查询货箱数据再查询货架
        if (StringUtils.isNotEmpty(params.getSearchValue()) && NumberUtils.isActiveInteger(params.getSearchType())) {
            switch (params.getSearchType()) {
                case 1:
                    //1货架编号
                    shelfCode = params.getSearchValue();
                    break;
                case 2:
                case 4:
                    //2物料编号 4物料名称 | 物料id查询货箱 货箱关联货架
                    Material material;
                    if (params.getSearchType() == 2) {
                        material = materialDao.findMaterialByMaterialCode(params.getSearchValue());
                    } else {
                        material = materialDao.findMaterialByMaterialName(params.getSearchValue());
                    }
                    if (material == null) {
                        return null;
                    }
                    slotList = slotDao.findSlotsByMaterialId(material.getId());
                    if (slotList.isEmpty()) {
                        return null;
                    }
                    break;
                case 3:
                    //3生产订单编号
                    slotList = slotDao.findSlotsByProductionOrderCode(params.getSearchValue());
                    if (slotList.isEmpty()) {
                        return null;
                    }
                    break;
                default:
                    return null;

            }
        }

        //是否空货箱|2物料编号 4物料名称 搜索时默认为搜索的是非空货箱 则跳过空货箱的搜索条件
        if (params.getEmptyStatus() != null && shelfIds.isEmpty()) {
            if (params.getEmptyStatus()) {
                slotList = slotDao.getEmptySlots();
            } else {
                slotList = slotDao.getNotEmptySlots();
            }
            if (slotList.isEmpty()) {
                return null;
            }
        }
        slotList.forEach(x -> shelfIds.add(x.getShelfId()));
        String finalShelfCode = shelfCode;

        return (Specification<Shelf>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            //组织架构|非超级管理员角色
            if (!params.getUser().getRoleId().contains(RoleConstant.ADMIN_ROLE_ID_STR)) {
                if (params.getUser().getWorkshopId() > 0) {
                    list.add(criteriaBuilder.equal(root.get("workshopId").as(Long.class), params.getUser().getWorkshopId()));
                } else if (params.getUser().getFactoryId() > 0) {
                    list.add(criteriaBuilder.equal(root.get("factoryId").as(Long.class), params.getUser().getFactoryId()));
                } else {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(Long.class), params.getUser().getCompanyId()));
                }
            }

            //是否在地图上
            if (params.getMapStatus() != null) {
                list.add(criteriaBuilder.equal(root.get("mapStatus").as(Boolean.class), params.getMapStatus()));
            }

            //是否锁定
            if (params.getLockStatus() != null) {
                list.add(criteriaBuilder.equal(root.get("lockStatus").as(Boolean.class), params.getLockStatus()));
            }

            //货架编号
            if (finalShelfCode != null) {
                list.add(criteriaBuilder.equal(root.get("shelfCode").as(String.class), finalShelfCode));
            }

            //货架id
            if (!shelfIds.isEmpty()) {
                CriteriaBuilder.In<Object> shelfIdList = criteriaBuilder.in(root.get("id").as(Long.class));
                for (Long shelfId : shelfIds) {
                    shelfIdList.value(shelfId);
                }
                list.add(shelfIdList);
            }

            if (list.isEmpty()) {
                return null;
            }
            Predicate[] predicates = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(predicates));
        };
    }

    //shelf list获取货箱
    private List<Slot> slotListByShelf(List<Shelf> shelfList) {
        List<Long> shelfIds = new ArrayList<>();
        shelfList.forEach(x -> shelfIds.add(x.getId()));
        //货箱
        return slotDao.findSlotsByShelfIdIn(shelfIds);
    }

    //货箱list获取物料
    private List<Material> materialListBySlot(List<Slot> slotList) {
        if (slotList.isEmpty()) {
            return new ArrayList<>();
        }
        Set<Long> materialSet = new HashSet<>();
        slotList.forEach(x -> {
            if (x.getMaterialId() > 0) {
                materialSet.add(x.getMaterialId());
            }
        });
        if (materialSet.isEmpty()) {
            return new ArrayList<>();
        }
        return materialDao.findMaterialsByIdIn(materialSet);
    }

    //列表处理
    private List<ShelfListResponseVo> shelfListMixer(List<Shelf> shelfList) {
        List<ShelfListResponseVo> voList = new ArrayList<>();
        if (shelfList.isEmpty()) {
            return voList;
        }

        //物料
        List<Slot> slotList = this.slotListByShelf(shelfList);
        //货箱
        List<Material> materialList = this.materialListBySlot(slotList);

        for (Shelf shelf : shelfList) {
            ShelfListResponseVo vo = new ShelfListResponseVo();
            BeanUtils.copyProperties(shelf, vo);

            //货箱信息
            List<ShelfListSlotVo> slotVoList = new ArrayList<>();
            for (Slot slot : slotList) {
                if (slot.getShelfId().equals(shelf.getId())) {
                    ShelfListSlotVo voSlot = new ShelfListSlotVo();
                    BeanUtils.copyProperties(slot, voSlot);
                    //物料名称 物料编号
                    for (Material material : materialList) {
                        if (slot.getMaterialId().equals(material.getId())) {
                            voSlot.setMaterialCode(material.getMaterialCode());
                            voSlot.setMaterialName(material.getMaterialName());
                            break;
                        }
                    }
                    slotVoList.add(voSlot);
                }
            }
            vo.setList(slotVoList);
            voList.add(vo);
        }

        return voList;
    }

    //列表下载处理
    private List<ShelfListDownloadVo> shelfListDownloadMixer(List<Shelf> shelfList) {
        List<ShelfListDownloadVo> voList = new ArrayList<>();

        //物料
        List<Slot> slotList = this.slotListByShelf(shelfList);
        //货箱
        List<Material> materialList = this.materialListBySlot(slotList);

        for (Shelf shelf : shelfList) {
            for (Slot slot : slotList) {
                if (slot.getShelfId().equals(shelf.getId())) {
                    ShelfListDownloadVo vo = new ShelfListDownloadVo();
                    BeanUtils.copyProperties(shelf, vo);
                    if (shelf.getAxisX() > 0 || shelf.getAxisY() > 0) {
                        if (shelf.getWalkingStatus()) {
                            vo.setAxisText("移动中");
                        } else {
                            vo.setAxisText("X(" + shelf.getAxisX() + ")Y(" + shelf.getAxisY() + ")");
                        }
                    }
                    vo.setLockStatusText(shelf.getLockStatus() ? "是" : "否");
                    vo.setMapStatusText(shelf.getMapStatus() ? "是" : "否");

                    vo.setSlotCode(slot.getSlotCode());
                    //0正常 1返修 2换片
                    if (slot.getMaterialState() ==0) {
                        vo.setMaterialStateText("正常");
                    } else if (slot.getMaterialState() == 1) {
                        vo.setMaterialStateText("返修");
                    } else {
                        vo.setMaterialStateText("换片");
                    }
                    vo.setMaterialNumber(slot.getMaterialNumber());
                    vo.setProductionOrderCode(slot.getProductionOrderCode());
                    for (Material material : materialList) {
                        if (material.getId().equals(slot.getMaterialId())) {
                            vo.setMaterialName(material.getMaterialName());
                            vo.setMaterialCode(material.getMaterialCode());
                            break;
                        }
                    }
                    voList.add(vo);
                }
            }
        }
        return voList;
    }
}
