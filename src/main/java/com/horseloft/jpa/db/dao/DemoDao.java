package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemoDao extends CrudRepository<User, Integer>{

	@Query(value = "select * from t_user", nativeQuery = true)
	List<User> findList();
	
}
