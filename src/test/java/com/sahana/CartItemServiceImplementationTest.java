package com.sahana;

import com.sahana.exception.CartItemException;
import com.sahana.exception.UserException;
import com.sahana.modal.Cart;
import com.sahana.modal.CartItem;
import com.sahana.modal.Product;
import com.sahana.modal.User;
import com.sahana.repository.CartItemRepository;
import com.sahana.repository.CartRepository;
import com.sahana.service.CartItemServiceImplementation;
import com.sahana.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartItemServiceImplementationTest {

	@Mock
	private CartItemRepository cartItemRepository;

	@Mock
	private UserService userService;

	@Mock
	private CartRepository cartRepository;

	@InjectMocks
	private CartItemServiceImplementation cartItemServiceImplementation;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateCartItem() {
		Product product = new Product();
		product.setPrice(100);
		product.setDiscountedPrice(80);

		CartItem cartItem = new CartItem();
		cartItem.setProduct(product);

		CartItem savedCartItem = new CartItem();
		savedCartItem.setId(1L);
		savedCartItem.setProduct(product);
		savedCartItem.setQuantity(1);
		savedCartItem.setPrice(100);
		savedCartItem.setDiscountedPrice(80);

		when(cartItemRepository.save(cartItem)).thenReturn(savedCartItem);

		CartItem result = cartItemServiceImplementation.createCartItem(cartItem);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(100, result.getPrice());
		assertEquals(80, result.getDiscountedPrice());
	}

	@Test
	public void testRemoveCartItem_Success() throws CartItemException, UserException {
		CartItem cartItem = new CartItem();
		cartItem.setId(1L);
		cartItem.setUserId(1L);

		User user = new User();
		user.setId(1L);

		when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
		when(userService.findUserById(1L)).thenReturn(user);

		doNothing().when(cartItemRepository).deleteById(1L);

		cartItemServiceImplementation.removeCartItem(1L, 1L);

		verify(cartItemRepository, times(1)).deleteById(1L);
	}

	@Test
	public void testFindCartItemById_Success() throws CartItemException {
		CartItem cartItem = new CartItem();
		cartItem.setId(1L);

		when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));

		CartItem result = cartItemServiceImplementation.findCartItemById(1L);

		assertNotNull(result);
		assertEquals(1L, result.getId());
	}

	@Test
	public void testFindCartItemById_Failure() {
		when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

		CartItemException thrownException = assertThrows(CartItemException.class, () -> {
			cartItemServiceImplementation.findCartItemById(1L);
		});

		assertEquals("cartItem not found with id : 1", thrownException.getMessage());
	}
}
