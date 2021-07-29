package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.UserPicture;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Date: 2020/1/25 下午4:36
 * User: YHC
 * Desc:
 */
public interface UserPictureDao extends CrudRepository<UserPicture, Long> {

    //id查询
    UserPicture findUserPictureByIdAndDeleteStatus(Long id, Boolean deleteStatus);

    //user_id查询
    List<UserPicture> findUserPicturesByUserIdAndDeleteStatus(Long userId, Boolean deleteStatus);
}
