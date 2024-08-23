package com.sahana.service;

import java.util.List;

import com.sahana.exception.ProductException;
import com.sahana.modal.Review;
import com.sahana.modal.User;
import com.sahana.request.ReviewRequest;

public interface ReviewService {

	public Review createReview(ReviewRequest req,User user) throws ProductException;
	
	public List<Review> getAllReview(Long productId);
	
	
}
