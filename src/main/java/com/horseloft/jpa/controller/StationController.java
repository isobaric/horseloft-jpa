package com.horseloft.jpa.controller;

import com.horseloft.jpa.annotation.Structure;
import com.horseloft.jpa.enums.JpaDao;
import com.horseloft.jpa.service.StationService;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseListVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.station.StationDetailVo;
import com.horseloft.jpa.vo.station.StationEditAddVo;
import com.horseloft.jpa.vo.station.StationListRequestVo;
import com.horseloft.jpa.vo.station.StationListResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Date: 2020/2/3 下午4:38
 * User: YHC
 * Desc:
 */
@RestController
@Api(tags = "工位")
@RequestMapping("/station")
public class StationController {

    @Autowired
    private StationService stationService;

    @PostMapping("/add")
    @Structure(JpaDao.DEFAULT)
    @ApiOperation(value = "工位添加-编辑 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseIdVo> stationEditAdd(@Valid @RequestBody StationEditAddVo params) {
        return stationService.stationBuild(params);
    }

    @Structure(JpaDao.STATION)
    @PostMapping("/detail")
    @ApiOperation(value = "工位详情 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<StationDetailVo> getStationDetail(@Valid @RequestBody RequestIdVo params) {
        return stationService.getStationDetailById(params);
    }

    @Structure(JpaDao.STATION)
    @PostMapping("/del")
    @ApiOperation(value = "工位删除 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> stationDelete(@Valid @RequestBody RequestIdVo params) {
        return stationService.deleteStationById(params);
    }

    @PostMapping("/list")
    @ApiOperation(value = "工位列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseListVo<StationListResponseVo>> getStationList(@Valid @RequestBody StationListRequestVo params) {
        return stationService.stationListByCurrentUser(params);
    }

    @PostMapping("/download")
    @ApiOperation(value = "工位列表列表下载 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public void getStationListDownLoad(@Valid @RequestBody StationListRequestVo params, HttpServletResponse response) throws Exception {
         stationService.stationListDownload(params, response);
    }
}
