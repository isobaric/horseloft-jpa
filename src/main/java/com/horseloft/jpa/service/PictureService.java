package com.horseloft.jpa.service;

import com.horseloft.jpa.constant.FileConstant;
import com.horseloft.jpa.db.dao.PictureDao;
import com.horseloft.jpa.db.entity.Picture;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.exception.BusinessException;
import com.horseloft.jpa.vo.PictureResponseVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseVo;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Date: 2020/1/29 下午2:22
 * User: YHC
 * Desc: 图片
 */
@Service
public class PictureService {

    @Autowired
    private PictureDao pictureDao;

    /**
     * 图片上传并保存
     * @param file
     * @param belongId
     * @param type
     * @return
     */
    public ResponseVo<ResponseIdVo> pictureSave(MultipartFile file, Long belongId, Integer type) {
        if (file.isEmpty()) {
            return ResponseVo.ofError("上传不能为空");
        }
        //文件后缀
        String suffix = Objects.requireNonNull(file.getContentType()).substring(file.getContentType().indexOf("/") + 1);
        //存储路径
        String filePath = FileConstant.FILE_UPLOAD_PATH + System.currentTimeMillis() + RandomUtils.nextInt(1000, 9999) + "." + suffix;
        //存储路径生成文件
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new BusinessException(ResponseCode.SERVER_ERROR.getCode(), "图片上传失败");
        }
        //写入数据库
        Picture picture = new Picture();
        picture.setBelongId(belongId);
        picture.setPath(filePath);
        picture.setType(type);
        pictureDao.save(picture);

        ResponseIdVo vo = new ResponseIdVo();
        vo.setId(picture.getId());
        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 获取一个图片
     * @return
     */
    public ResponseVo<PictureResponseVo> getOnePicture(Long belongId, Integer type) {
        Picture picture = pictureDao.getPictureByBelongId(belongId, type);
        if (picture == null) {
            return ResponseVo.ofError("图片不存在");
        }
        PictureResponseVo vo = new PictureResponseVo();
        vo.setId(picture.getId());
        vo.setPath(picture.getPath());
        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 获取图片列表
     * @param belongId
     * @param type
     * @return
     */
    public ResponseVo<List<PictureResponseVo>> getPictureList(Long belongId, Integer type) {
        List<Picture> pictureList = pictureDao.getPicturesByBelongId(belongId, type);
        List<PictureResponseVo> voList = new ArrayList<>();
        for (Picture picture : pictureList) {
            PictureResponseVo vo = new PictureResponseVo();
            vo.setId(picture.getId());
            vo.setPath(picture.getPath());
            voList.add(vo);
        }
        return ResponseVo.ofSuccess(voList);
    }

    /**
     * 图片删除
     * @param id
     * @param type
     * @return
     */
    public ResponseVo<String> pictureDeleteById(Long id, Integer type) {
        Picture picture = pictureDao.getPictureByIdType(id, type);
        if (picture != null) {
            picture.setDeleteStatus(true);
            pictureDao.save(picture);
        }
        return ResponseVo.ofSuccess();
    }
}
