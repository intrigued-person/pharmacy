package com.sahana.service;

import com.sahana.exception.ProductException;
import com.sahana.modal.Cart;
import com.sahana.modal.CartItem;
import com.sahana.modal.User;
import com.sahana.request.AddItemRequest;

public interface CartService {
	
	public Cart createCart(User user);
	
	public CartItem addCartItem(Long userId,AddItemRequest req) throws ProductException;
	
	public Cart findUserCart(Long userId);

}
