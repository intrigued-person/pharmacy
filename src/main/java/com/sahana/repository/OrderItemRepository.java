package com.sahana.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sahana.modal.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
