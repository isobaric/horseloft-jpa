package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.Slot;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Date: 2020/2/5 下午2:40
 * User: YHC
 * Desc: 货箱
 */
public interface SlotDao extends CrudRepository<Slot, Long>, JpaSpecificationExecutor<Slot> {

    //shelf_id查询
    List<Slot> findSlotsByShelfId(Long shelfId);

    //shelf_id in 查询
    List<Slot> findSlotsByShelfIdIn(List<Long> shelfIds);

    //shelf_id删除
    void deleteSlotsByShelfId(Long shelfId);

    //生产订单编号查询列表
    List<Slot> findSlotsByProductionOrderCode(String productionOrderCode);

    //物料id 查询列表
    List<Slot> findSlotsByMaterialId(Long materialId);

    //空货箱
    @Query(value = "select * from jpa_slot where material_id = 0", nativeQuery = true)
    List<Slot> getEmptySlots();

    //空货箱
    @Query(value = "select * from jpa_slot where material_id > 0", nativeQuery = true)
    List<Slot> getNotEmptySlots();
}
