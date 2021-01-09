package com.iftm.course.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iftm.course.dto.CategoryDto;
import com.iftm.course.dto.ProductCategoriesDto;
import com.iftm.course.dto.ProductDto;
import com.iftm.course.entities.Product;
import com.iftm.course.repositories.CategoryRepository;
import com.iftm.course.repositories.ProductRepository;
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
}