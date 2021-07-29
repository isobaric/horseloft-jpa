package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.Picture;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2020/1/29 下午2:38
 * User: YHC
 * Desc: 图片
 */
@Repository
public interface PictureDao extends CrudRepository<Picture, Long> {

    //type belong_id 查询多条
    @Query(value = "select * from jpa_picture where delete_status = 0 and type =:type and belong_id = :belongId", nativeQuery = true)
    List<Picture> getPicturesByBelongId(@Param("belongId") Long belongId, @Param("type") Integer type);

    //type belong_id 查询一条
    @Query(value = "select * from jpa_picture where delete_status = 0 and type =:type and belong_id = :belongId limit 1", nativeQuery = true)
    Picture getPictureByBelongId(@Param("belongId") Long belongId, @Param("type") Integer type);

    //type id delete_status 查询一条
    @Query(value = "select * from jpa_picture where delete_status = 0 and type =:type and id = :id", nativeQuery = true)
    Picture getPictureByIdType(@Param("id") Long id, @Param("type") Integer type);

    //id delete_status 查询一条
    Picture findPictureByIdAndDeleteStatus(Long id, Boolean deleteStatus);

    //type delete_status 列表
    List<Picture> findPicturesByTypeAndDeleteStatus(Integer type, Boolean deleteStatus);
}
