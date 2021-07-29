package com.horseloft.jpa.service;

import com.horseloft.jpa.db.dao.DistrictDao;
import com.horseloft.jpa.db.entity.District;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.structure.DistrictResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2020/1/7 15:52
 * User: YHC
 * Desc:
 */
@Service
public class DistrictService {

    @Autowired
    private DistrictDao districtDao;

    /**
     * 省市县三级列表
     * @return
     */
    public ResponseVo<List<DistrictResponseVo>> getDistrictAll() {
        List<District> districtList = (List<District>) districtDao.findAll();

        List<DistrictResponseVo> provinceList = new ArrayList<>();
        List<DistrictResponseVo> cityList = new ArrayList<>();
        List<DistrictResponseVo> areaList = new ArrayList<>();
        for(District district : districtList) {
            //省
            if (district.getRank().equals(1)) {
                DistrictResponseVo province = new DistrictResponseVo();
                BeanUtils.copyProperties(district, province);
                provinceList.add(province);
            }
            //市
            if (district.getRank().equals(2)) {
                DistrictResponseVo city = new DistrictResponseVo();
                BeanUtils.copyProperties(district, city);
                cityList.add(city);
            }
            //区县
            if (district.getRank().equals(3)) {
                DistrictResponseVo area = new DistrictResponseVo();
                BeanUtils.copyProperties(district, area);
                areaList.add(area);
            }
        }

        List<DistrictResponseVo> provinceArr = new ArrayList<>();
        for(DistrictResponseVo disPro: provinceList) {

            List<DistrictResponseVo> cityArr = new ArrayList<>();
            for(DistrictResponseVo disCity: cityList) {

                if (disCity.getPid().equals(disPro.getId())) {
                    List<DistrictResponseVo> areaArr = new ArrayList<>();
                    for(DistrictResponseVo disArea: areaList) {
                        if (disArea.getPid().equals(disCity.getId())) {
                            areaArr.add(disArea);
                        }
                    }
                    disCity.setList(areaArr);
                    cityArr.add(disCity);
                }
            }
            disPro.setList(cityArr);
            provinceArr.add(disPro);
        }
        return ResponseVo.ofSuccess(provinceArr);
    }
}
