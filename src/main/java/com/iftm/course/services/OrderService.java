package com.iftm.course.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iftm.course.dto.OrderDto;
import com.iftm.course.repositories.OrderRepository;
import com.iftm.course.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	public List<OrderDto> findAll() {
		return orderRepository.findAll().stream().map(e -> new OrderDto(e)).collect(Collectors.toList());
	}
	
	public OrderDto findById(Long id) {		
		return new OrderDto(orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)));
	}
}
