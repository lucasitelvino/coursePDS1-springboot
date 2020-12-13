package com.iftm.course.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iftm.course.entities.User;

@RestController
@RequestMapping(value = "/users")
public class UserResource {
	
	@GetMapping
	public 	ResponseEntity<User> findAll() {
		User u = new User(1L, "Lucas", "lucas@gmail.com", "(34) 99999-9999", "123456");
		return ResponseEntity.ok().body(u);
	}
}
