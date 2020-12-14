package com.iftm.course.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iftm.course.dto.ProductDto;
import com.iftm.course.repositories.ProductRepository;
import com.iftm.course.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public List<ProductDto> findAll() {
		return productRepository.findAll().stream().map(e -> new ProductDto(e)).collect(Collectors.toList());
	}

	public ProductDto findById(Long id) {
		return new ProductDto(productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)));
	}

}