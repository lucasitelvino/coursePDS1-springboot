package com.iftm.course.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iftm.course.dto.UserDto;
import com.iftm.course.dto.UserInsertDto;
import com.iftm.course.entities.User;
import com.iftm.course.repositories.UserRepository;
import com.iftm.course.services.exceptions.DatabaseException;
import com.iftm.course.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<UserDto> findAll() {
		return userRepository.findAll().stream().map(user -> new UserDto(user)).collect(Collectors.toList());
	}
	
	public UserDto findById(Long id) {
		return new UserDto(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)));
	}
	
	@Transactional
	public UserDto insert(UserInsertDto dto) {
		return new UserDto(userRepository.save(dto.toEntity()));
	}
	
	public void delete(Long id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
		
	}
	
	@Transactional
	public UserDto update(Long id, UserDto dto) {
		try {
			User entity = userRepository.getOne(id);
			updateData(entity, dto);
			return new UserDto(userRepository.save(entity));
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(User entity, UserDto dto) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setPhone(dto.getPhone());
	}
}
