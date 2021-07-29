package com.horseloft.jpa.service;

import com.horseloft.jpa.constant.FileConstant;
import com.horseloft.jpa.db.dao.UserDao;
import com.horseloft.jpa.db.dao.UserPictureDao;
import com.horseloft.jpa.db.entity.User;
import com.horseloft.jpa.db.entity.UserPicture;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.user.UserIdPictureResponseVo;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2020/1/25 下午4:38
 * User: YHC
 * Desc:
 */
@Service
public class UserPictureService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserPictureDao userPictureDao;

    public ResponseVo<ResponseIdVo> userPictureUpload(MultipartFile file, Long userId) {
        if (userId == null || userId <= 0) {
            return ResponseVo.ofError("用户ID错误");
        }
        if (file.isEmpty()) {
            return ResponseVo.ofError("身份证上传不能为空");
        }
        String fileType = file.getContentType();
        if (fileType == null
                && !fileType.toLowerCase().equals("image/png")
                && !fileType.toLowerCase().equals("image/gif")
                && !fileType.toLowerCase().equals("image/bmp")
                && !fileType.toLowerCase().equals("image/jpg")
                && !fileType.toLowerCase().equals("image/jpeg"))
        {
            return ResponseVo.ofError("仅支持png jpg jpeg图片");
        }

        //查询当前用户
        User user = userDao.findUserByIdAndDeleteStatus(userId, false);
        if (user == null) {
            return ResponseVo.ofError("用户信息不存在");
        }

        //每个用户的身份证图片不能超过2个
        List<UserPicture> userPictureList = userPictureDao.findUserPicturesByUserIdAndDeleteStatus(userId, false);
        if (userPictureList.size() >= 2) {
            return ResponseVo.ofError("仅允许上传两张身份证照片");
        }

        //文件后缀
        String suffix = fileType.substring(fileType.indexOf("/") + 1);
        //存储路径
        String filePath = FileConstant.FILE_UPLOAD_PATH + System.currentTimeMillis() + RandomUtils.nextInt(1000, 9999) + "." + suffix;
        //存储路径生成文件
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            return ResponseVo.ofError("上传失败");
        }
        //数据写入
        UserPicture userPicture = new UserPicture();
        userPicture.setPath(filePath);
        userPicture.setUserId(userId);
        userPictureDao.save(userPicture);

        ResponseIdVo responseIdVo = new ResponseIdVo();
        responseIdVo.setId(userPicture.getId());
        return ResponseVo.ofSuccess(responseIdVo);
    }

    /**
     * 身份证照片删除
     * @param params
     * @return
     */
    public ResponseVo<String> userPictureDeleteById(RequestIdVo params) {
        UserPicture userPicture = userPictureDao.findUserPictureByIdAndDeleteStatus(params.getId(), false);
        if (userPicture != null) {
            userPicture.setDeleteStatus(true);
            userPictureDao.save(userPicture);
        }
        return ResponseVo.ofSuccess();
    }

    /**
     * 用户身份证列表
     * @param params
     * @return
     */
    public ResponseVo<List<UserIdPictureResponseVo>> userPictureListById(RequestIdVo params) {
        List<UserIdPictureResponseVo> list = new ArrayList<>();
        List<UserPicture> userPictureList = userPictureDao.findUserPicturesByUserIdAndDeleteStatus(params.getId(), false);

        userPictureList.forEach(x -> {
            UserIdPictureResponseVo vo = new UserIdPictureResponseVo();
            BeanUtils.copyProperties(x, vo);
            list.add(vo);
        });
        return ResponseVo.ofSuccess(list);
    }
}
