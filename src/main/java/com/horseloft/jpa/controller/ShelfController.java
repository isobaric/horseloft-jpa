package com.horseloft.jpa.controller;

import com.horseloft.jpa.annotation.Structure;
import com.horseloft.jpa.enums.JpaDao;
import com.horseloft.jpa.service.ShelfService;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseListVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.shelf.ShelfAddRequestVo;
import com.horseloft.jpa.vo.shelf.ShelfDetailResponseVo;
import com.horseloft.jpa.vo.shelf.ShelfListRequestVo;
import com.horseloft.jpa.vo.shelf.ShelfListResponseVo;
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
 * Date: 2020/2/4 下午3:11
 * User: YHC
 * Desc: 货架
 */
@RestController
@RequestMapping("/shelf")
@Api(tags = "货架")
public class ShelfController {

    @Autowired
    private ShelfService shelfService;

    @PostMapping("/add")
    @Structure(JpaDao.DEFAULT)
    @ApiOperation(value = "新增 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseIdVo> shelfAdd(@Valid @RequestBody ShelfAddRequestVo params) {
        return shelfService.shelfAddByCurrentUser(params);
    }

    @Structure(JpaDao.SHELF)
    @PostMapping("/detail")
    @ApiOperation(value = "详情 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ShelfDetailResponseVo> getShelfDetail(@Valid @RequestBody RequestIdVo params) {
        return shelfService.getShelfDetailById(params);
    }

    @Structure(JpaDao.SHELF)
    @PostMapping("/del")
    @ApiOperation(value = "删除 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> shelfDelete(@Valid @RequestBody RequestIdVo params) {
        return shelfService.deleteShelfById(params);
    }

    @PostMapping("/list")
    @ApiOperation(value = "列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseListVo<ShelfListResponseVo>> getShelfList(@Valid @RequestBody ShelfListRequestVo params) {
        return shelfService.getShelfListByCurrentUser(params);
    }

    @PostMapping("/download")
    @ApiOperation(value = "下载 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public void getShelfListDownload(@Valid @RequestBody ShelfListRequestVo params, HttpServletResponse response) throws Exception {
        shelfService.shelfListDownload(params, response);
    }

    //TODO 对接AMS之后再做
    @Structure(JpaDao.SHELF)
    @PostMapping("/empty")
    @ApiOperation(value = "腾空货架", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> shelfSetEmpty(@Valid @RequestBody RequestIdVo params) {
        return ResponseVo.ofSuccess();
    }

}
