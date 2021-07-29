package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.Company;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Date: 2020/1/7 16:13
 * User: YHC
 * Desc:
 */
@Repository
public interface CompanyDao extends CrudRepository<Company, Long> {

    //id delete_status查询一条
    Company findCompanyByIdAndDeleteStatus(Long id, Boolean deleteStatus);

    //id in查询
    List<Company> findCompaniesByIdIn(List<Long> id);

    //公司名称查找公司
    @Query(value = "select * from jpa_structure_company where com_name = ?", nativeQuery = true)
    Company getBaseInfoByComName(String comName);

    //公司编码查找公司
    @Query(value = "select * from jpa_structure_company where com_code = ?", nativeQuery = true)
    Company getBaseInfoByComCode(String comCode);

    //全部列表
    @Query(value = "select * from jpa_structure_company where delete_status = 0", nativeQuery = true)
    List<Company> getAllCompany();

    //删除公司|更改名称、code
    @Modifying
    @Transactional
    @Query(value = "update jpa_structure_company set delete_status = 1 where id = ?", nativeQuery = true)
    int delCompanyById(Long id);
}
