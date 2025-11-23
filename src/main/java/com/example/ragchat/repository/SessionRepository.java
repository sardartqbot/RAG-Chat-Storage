package com.example.ragchat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ragchat.entity.Session;

public interface SessionRepository extends JpaRepository<Session, String> {
	@Query("SELECT s FROM Session s WHERE s.user.id = :userId")
	List<Session> findByUserId(@Param("userId") String userId);
}
