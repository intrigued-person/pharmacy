package com.sahana;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sahana.exception.ProductException;
import com.sahana.modal.Cart;
import com.sahana.modal.CartItem;
import com.sahana.modal.Product;
import com.sahana.modal.User;
import com.sahana.repository.CartRepository;
import com.sahana.request.AddItemRequest;
import com.sahana.service.CartItemService;
import com.sahana.service.CartServiceImplementation;
import com.sahana.service.ProductService;

public class CartServiceImplementationTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImplementation cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCart() {
        // Prepare mock data
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();
        cart.setUser(user);

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart createdCart = cartService.createCart(user);

        assertEquals(user, createdCart.getUser());
    }

    @Test
    void testAddCartItem_Success() throws ProductException {
        // Prepare mock data
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());

        Product product = new Product();
        product.setId(1L);
        product.setDiscountedPrice(18);

        AddItemRequest addItemRequest = new AddItemRequest();
        addItemRequest.setProductId(product.getId());
        addItemRequest.setQuantity(2);
        addItemRequest.setSize("M");

        CartItem existingCartItem = null;
        CartItem newCartItem = new CartItem();
        newCartItem.setPrice(36);
        newCartItem.setDiscountedPrice(36);
        newCartItem.setQuantity(2);
        newCartItem.setSize("M");
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setUserId(user.getId());

        when(cartRepository.findByUserId(anyLong())).thenReturn(cart);
        when(productService.findProductById(anyLong())).thenReturn(product);
        when(cartItemService.isCartItemExist(any(Cart.class), any(Product.class), any(String.class), anyLong()))
                .thenReturn(existingCartItem);
        when(cartItemService.createCartItem(any(CartItem.class))).thenReturn(newCartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartItem addedCartItem = cartService.addCartItem(1L, addItemRequest);

        assertEquals(newCartItem, addedCartItem);
    }

    @Test
    void testAddCartItem_ProductException() throws ProductException {
        // Prepare mock data
        User user = new User();
        user.setId(1L);

        AddItemRequest addItemRequest = new AddItemRequest();
        addItemRequest.setProductId(1L);
        addItemRequest.setQuantity(2);
        addItemRequest.setSize("M");

        when(cartRepository.findByUserId(anyLong())).thenReturn(new Cart());
        when(productService.findProductById(anyLong())).thenThrow(new ProductException("Product not found"));

        assertThrows(ProductException.class, () -> {
            cartService.addCartItem(1L, addItemRequest);
        });
    }
}

