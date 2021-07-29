package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.Workshop;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2020/1/7 16:49
 * User: YHC
 * Desc:
 */
@Repository
public interface WorkshopDao extends CrudRepository<Workshop, Long> {

    //查询一条
    Workshop findWorkshopByIdAndDeleteStatus(Long id, Boolean deleteStatus);

    //id in查询
    List<Workshop> findWorkshopsByIdIn(List<Long> id);

    @Query(value = "select * from jpa_structure_workshop where delete_status = 0", nativeQuery = true)
    List<Workshop> getAllWorkshop();

    //车间名称获取工厂的车间信息|唯一索引 所以为一条数据
    @Query(value = "select * from jpa_structure_workshop where delete_status = 0 and factory_id = :factoryId and workshop_name = :workshopName", nativeQuery = true)
    Workshop getFactoryWorkshopByWorkshopName(@Param("factoryId") Long factoryId, @Param("workshopName") String workshopName);

    //车间编码获取工厂的车间信息|唯一索引 所以为一条数据
    @Query(value = "select * from jpa_structure_workshop where delete_status = 0 and factory_id = :factoryId and workshop_code = :workshopCode", nativeQuery = true)
    Workshop getFactoryWorkshopByWorkshopCode(@Param("factoryId") Long factoryId, @Param("workshopCode") String workshopCode);

    //工厂id获取全部车间
    @Query(value = "select * from jpa_structure_workshop where delete_status = 0 and factory_id = ?", nativeQuery = true)
    List<Workshop> getAllFactoryWorkshop(Long factoryId);

    //公司的全部车间
    @Query(value = "select * from jpa_structure_workshop where delete_status = 0 and company_id = ?", nativeQuery = true)
    List<Workshop> getAllCompanyWorkshop(Long companyId);

    //公司id、车间id 查询一条
    @Query(value = "select * from jpa_structure_workshop where delete_status = 0 and company_id = :companyId and id = :id", nativeQuery = true)
    Workshop getWorkshopByCompanyIdAndId(@Param("companyId") Long companyId, @Param("id") Long id);
}
