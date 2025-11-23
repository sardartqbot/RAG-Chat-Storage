package com.example.ragchat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ragchat.entity.Message;

public interface MessageRepository extends JpaRepository<Message, String> {
	@Query("SELECT m FROM Message m WHERE m.session.id = :sessionId ORDER BY m.createdAt ASC")
	Page<Message> findBySessionId(@Param("sessionId") String sessionId, Pageable pageable);
}
