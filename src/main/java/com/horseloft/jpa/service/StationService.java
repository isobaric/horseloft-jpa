package com.horseloft.jpa.service;

import com.horseloft.jpa.db.dao.StationDao;
import com.horseloft.jpa.db.dao.StationTypeDao;
import com.horseloft.jpa.db.entity.Station;
import com.horseloft.jpa.db.entity.StationType;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.exception.BusinessException;
import com.horseloft.jpa.utils.FileUtils;
import com.horseloft.jpa.utils.NumberUtils;
import com.horseloft.jpa.utils.StringUtils;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseListVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.station.StationDetailVo;
import com.horseloft.jpa.vo.station.StationEditAddVo;
import com.horseloft.jpa.vo.station.StationListRequestVo;
import com.horseloft.jpa.vo.station.StationListResponseVo;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2020/2/3 下午4:59
 * User: YHC
 * Desc: 工位
 */
@Service
public class StationService {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private StationTypeDao stationTypeDao;

    @Autowired
    private PublicService publicService;

    /**
     * 工位编辑-添加
     * @param params
     * @return
     */
    public ResponseVo<ResponseIdVo> stationBuild(StationEditAddVo params) {
        //编号不能重复 工位类型暂时只有1-8 不做数据库判断
        Station station = stationDao.findStationByStationCode(params.getStationCode());
        if (params.getId() > 0) {
            if (station != null && !station.getId().equals(params.getId())) {
                return ResponseVo.ofError("工位编号已存在");
            }
        } else {
            if (station != null) {
                return ResponseVo.ofError("工位编号已存在");
            }
        }
        Station stationEdit;
        if (params.getId() > 0) {
            stationEdit = stationDao.findById(params.getId()).orElse(null);
            if (stationEdit == null) {
                return ResponseVo.ofError("工位信息异常，请刷新页面重试");
            }
            if (stationEdit.getMapStatus()) {
                return ResponseVo.ofError("工位正在使用，无法编辑");
            }
        } else {
            stationEdit = new Station();
        }

        stationEdit.setPassStatus(params.getPassStatus());
        stationEdit.setCompanyId(params.getCompanyId());
        stationEdit.setFactoryId(params.getFactoryId());
        stationEdit.setWorkshopId(params.getWorkshopId());
        stationEdit.setStationCode(params.getStationCode());
        stationEdit.setStationTypeId(params.getStationTypeId());
        stationDao.save(stationEdit);

        ResponseIdVo vo = new ResponseIdVo();
        vo.setId(stationEdit.getId());
        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 工位详情
     * @param params
     * @return
     */
    public ResponseVo<StationDetailVo> getStationDetailById(RequestIdVo params) {
        Station station = stationDao.findById(params.getId()).orElse(null);
        if (station == null) {
            return ResponseVo.ofError("工位信息不存在");
        }
        StationDetailVo vo = new StationDetailVo();
        BeanUtils.copyProperties(station, vo);

        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 工位删除|物理删除
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<String> deleteStationById(RequestIdVo params) {
        Station station = stationDao.findById(params.getId()).orElse(null);
        if (station != null) {
            if (station.getMapStatus()) {
                return ResponseVo.ofError("工位正在使用，无法删除！");
            }
            stationDao.deleteById(params.getId());
        }
        return ResponseVo.ofSuccess();
    }

    /**
     * 工位列表下载
     * @param params
     * @param response
     * @throws Exception
     */
    public void stationListDownload(StationListRequestVo params, HttpServletResponse response) throws Exception {
        Sort sort = Sort.by(Sort.Order.asc("id"));
        List<Station> list = stationDao.findAll(this.stationSpecification(params), sort);
        if (list.isEmpty()) {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "您下载的数据为空");
        }
        List<StationListResponseVo> voList = this.stationListMixer(list, true);
        //下载
        FileUtils.excelDownload(response, voList, "工位列表");
    }

    /**
     * 工位列表
     * @param params
     * @return
     */
    public ResponseVo<ResponseListVo<StationListResponseVo>> stationListByCurrentUser(StationListRequestVo params) {
        Sort sort = Sort.by(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), sort);
        Page<Station> page = stationDao.findAll(this.stationSpecification(params), pageable);

        ResponseListVo<StationListResponseVo> listVo = new ResponseListVo<>();
        listVo.setPage(params.getPage());
        listVo.setPageSize(params.getPageSize());
        listVo.setTotal(page.getTotalElements());
        listVo.setTotalPage(page.getTotalPages());
        listVo.setList(this.stationListMixer(page.getContent(), false));

        return ResponseVo.ofSuccess(listVo);
    }

    //工位列表查询条件
    private Specification<Station> stationSpecification(StationListRequestVo params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            //组织架构
            PublicService publicService = new PublicService();
            Map<String, Object> map = publicService.getSearchStructure(params.getUser(), params.getCompanyId(), params.getFactoryId(), params.getWorkshopId());
            if (!map.isEmpty()) {
                list.add(criteriaBuilder.equal(root.get(map.get("name").toString()).as(Long.class), map.get("value")));
            }
            //搜索框
            if (StringUtils.isNotEmpty(params.getSearchValue())) {
                list.add(criteriaBuilder.like(root.get("stationCode").as(String.class), "%" + params.getSearchValue() + "%"));
            }
            //工位类型id 1-8 不验证数据库
            if (NumberUtils.isActiveLong(params.getStationTypeId())) {
                list.add(criteriaBuilder.equal(root.get("stationTypeId").as(Long.class), params.getStationTypeId()));
            }
            //下方是否可通行
            if (params.getPassStatus() != null) {
                list.add(criteriaBuilder.equal(root.get("passStatus").as(Boolean.class), params.getPassStatus()));
            }

            if (list.isEmpty()) {
                return null;
            }
            Predicate[] predicates = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(predicates));
        };
    }

    //工位列表数据整合
    private List<StationListResponseVo> stationListMixer(List<Station> list, boolean isDownload) {
        List<StationListResponseVo> voList = new ArrayList<>();
        //组织架构
        Map<String, List<?>> listMap = publicService.getStructureDataByList(list);
        //工位类型
        Iterable<StationType> stationTypes = stationTypeDao.findAll();

        for (Station station : list) {
            StationListResponseVo vo = new StationListResponseVo();
            BeanUtils.copyProperties(station, vo);

            //组织架构|格式：公司名-工厂名-车间名
            String structureName = publicService.getStructureName(listMap, station.getCompanyId(),station.getFactoryId(), station.getWorkshopId());
            vo.setStructure(structureName);

            //工位类型名称
            for (StationType stationType : stationTypes) {
                if (station.getStationTypeId().equals(stationType.getId())) {
                    vo.setStationTypeName(stationType.getTypeName());
                    break;
                }
            }

            if (isDownload) {
                vo.setMapStatusText(station.getMapStatus() ? "是" : "否");
                vo.setPassStatusText(station.getPassStatus() ? "是" : "否");
                if (station.getMapStatus()) {
                    vo.setAxis("X (" + station.getAxisX() + ") Y (" + station.getAxisY() + ")");
                }
            }

            voList.add(vo);
        }
        return voList;
    }

}
