package com.sahana;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sahana.exception.OrderException;

import com.sahana.modal.Order;
import com.sahana.repository.AddressRepository;
import com.sahana.repository.OrderItemRepository;
import com.sahana.repository.OrderRepository;
import com.sahana.repository.UserRepository;
import com.sahana.service.CartService;
import com.sahana.service.OrderItemService;
import com.sahana.service.OrderServiceImplementation;
import com.sahana.user.domain.OrderStatus;

public class OrderServiceImplementationTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private CartService cartService;

	@Mock
	private AddressRepository addressRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private OrderItemService orderItemService;

	@Mock
	private OrderItemRepository orderItemRepository;

	@InjectMocks
	private OrderServiceImplementation orderService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindOrderById_Success() throws OrderException {
		// Prepare mock data
		Order order = new Order();
		order.setId(1L);

		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

		Order foundOrder = orderService.findOrderById(1L);

		assertEquals(1L, foundOrder.getId());
	}

	@Test
	void testFindOrderById_NotFound() throws OrderException {
		when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(OrderException.class, () -> {
			orderService.findOrderById(1L);
		});
	}

	@Test
	void testUpdateOrderStatus() throws OrderException {
		// Prepare mock data
		Order order = new Order();
		order.setId(1L);

		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		Order confirmedOrder = orderService.confirmedOrder(1L);

		assertEquals(OrderStatus.CONFIRMED, confirmedOrder.getOrderStatus());
	}

	@Test
	void testDeleteOrder() throws OrderException {
		// Prepare mock data
		Order order = new Order();
		order.setId(1L);

		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

		orderService.deleteOrder(1L);

		// Verify that delete method is called
		verify(orderRepository).deleteById(anyLong());
	}

	@Test
	void testUsersOrderHistory() {
		// Prepare mock data
		Order order = new Order();
		order.setId(1L);

		List<Order> orders = List.of(order);

		when(orderRepository.getUsersOrders(anyLong())).thenReturn(orders);

		List<Order> result = orderService.usersOrderHistory(1L);

		assertEquals(1, result.size());
		assertEquals(1L, result.get(0).getId());
	}

	@Test
	void testGetAllOrders() {
		// Prepare mock data
		Order order1 = new Order();
		Order order2 = new Order();
		List<Order> orders = List.of(order1, order2);

		when(orderRepository.findAllByOrderByCreatedAtDesc()).thenReturn(orders);

		List<Order> result = orderService.getAllOrders();

		assertEquals(2, result.size());
	}
}
