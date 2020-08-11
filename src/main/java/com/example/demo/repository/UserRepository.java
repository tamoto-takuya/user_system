package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select t from User t where t.loginId = :loginId AND t.id <> :id")
	List<User> findByIdOriginal(@Param("loginId") String loginId, @Param("id") Integer id);

	List<User> findByLoginIdContaining(String loginId);


//	List<User> findByLoginIdContaining(String loginId);
}
