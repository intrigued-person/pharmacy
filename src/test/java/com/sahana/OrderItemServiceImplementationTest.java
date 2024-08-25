package com.sahana;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sahana.modal.OrderItem;
import com.sahana.modal.Product;
import com.sahana.repository.OrderItemRepository;
import com.sahana.service.OrderItemServiceImplementation;

public class OrderItemServiceImplementationTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemServiceImplementation orderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrderItem_Success() {
        // Prepare mock data
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(20);
        orderItem.setDiscountedPrice(18);
        orderItem.setQuantity(2);
        orderItem.setSize("M");
        orderItem.setProduct(new Product());
        orderItem.setUserId(1L);

        // Mock repository behavior
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

        // Call the service method
        OrderItem createdOrderItem = orderItemService.createOrderItem(orderItem);

        // Assert the result
        assertEquals(orderItem, createdOrderItem);
    }
}
