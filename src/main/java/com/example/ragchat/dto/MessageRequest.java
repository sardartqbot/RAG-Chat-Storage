package com.example.ragchat.dto;

import java.util.HashMap;
import java.util.Map;

import com.example.ragchat.entity.Sender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
	Sender sender;
	String content;
	private Map<String, Object> context = new HashMap<>();

}
