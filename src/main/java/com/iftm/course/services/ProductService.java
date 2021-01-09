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
import com.iftm.course.dto.ProductCategoriesDto;
import com.iftm.course.dto.ProductDto;
import com.iftm.course.entities.Product;
import com.iftm.course.repositories.CategoryRepository;
import com.iftm.course.repositories.ProductRepository;
import com.iftm.course.services.exceptions.DatabaseException;
import com.iftm.course.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	public List<ProductDto> findAll() {
		return productRepository.findAll().stream().map(e -> new ProductDto(e)).collect(Collectors.toList());
	}

	public ProductDto findById(Long id) {
		return new ProductDto(productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)));
	}

	@Transactional
	public ProductDto insert(ProductCategoriesDto dto) {
		Product entity = dto.toEntity();
		setProductCategories(entity, dto.getCategories());
		return new ProductDto(productRepository.save(entity));
	}
	
	private void setProductCategories(Product entity, List<CategoryDto> categories) {
		entity.getCategories().clear();
		for (CategoryDto dto : categories) entity.getCategories().add(categoryRepository.getOne(dto.getId()));
	}
	
	
	public void delete(Long id) {
		try {
			productRepository.deleteById(id);
		} catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch(DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage()); 
		}
	}
	
	@Transactional
	public ProductDto update(Long id, ProductCategoriesDto dto) {
		try {
			Product entity = productRepository.getOne(id);
			updateData(entity, dto);
			return new ProductDto(productRepository.save(entity));
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	private void updateData(Product entity, ProductCategoriesDto dto) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		if (dto.getCategories() != null && dto.getCategories().size() > 0) setProductCategories(entity, dto.getCategories());
	}
}