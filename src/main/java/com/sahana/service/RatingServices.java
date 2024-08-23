package com.sahana.service;

import java.util.List;

import com.sahana.exception.ProductException;
import com.sahana.modal.Rating;
import com.sahana.modal.User;
import com.sahana.request.RatingRequest;

public interface RatingServices {
	
	public Rating createRating(RatingRequest req,User user) throws ProductException;
	
	public List<Rating> getProductsRating(Long productId);

}
