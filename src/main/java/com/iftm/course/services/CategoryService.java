package com.iftm.course.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iftm.course.dto.CategoryDto;
import com.iftm.course.entities.Category;
import com.iftm.course.repositories.CategoryRepository;
import com.iftm.course.services.exceptions.DatabaseException;
import com.iftm.course.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public List<CategoryDto> findAll() {
		return categoryRepository.findAll().stream().map(e -> new CategoryDto(e)).collect(Collectors.toList());
	}

	public CategoryDto findById(Long id) {
		return new CategoryDto(categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)));
	}

	public CategoryDto insert(CategoryDto dto) {
		return new CategoryDto(categoryRepository.save(dto.toEntity()));
	}

	@Transactional
	public CategoryDto update(Long id, CategoryDto dto) {
		try {
			Category entity = categoryRepository.getOne(id);
			updateData(entity, dto);
			return new CategoryDto(categoryRepository.save(entity));
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Category entity, CategoryDto dto) {
		entity.setName(dto.getName());
	}

	public void delete(Long id) {
		try {
			categoryRepository.deleteById(id);
		} catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch(DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage()); 
		}
	}
}