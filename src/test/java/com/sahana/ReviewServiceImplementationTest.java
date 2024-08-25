package com.sahana;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sahana.exception.ProductException;
import com.sahana.modal.Product;
import com.sahana.modal.Review;
import com.sahana.modal.User;
import com.sahana.repository.ProductRepository;
import com.sahana.repository.ReviewRepository;
import com.sahana.request.ReviewRequest;
import com.sahana.service.ProductService;
import com.sahana.service.ReviewServiceImplementation;

public class ReviewServiceImplementationTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewServiceImplementation reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReview_Success() throws ProductException {
        // Prepare mock data
        Product product = new Product();
        product.setId(1L);
        product.setReviews(new ArrayList<>());

        User user = new User();

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setProductId(1L);
        reviewRequest.setReview("Great product!");

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReview("Great product!");
        review.setCreatedAt(LocalDateTime.now());

        // Mocking the behavior of productService and repositories
        when(productService.findProductById(1L)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // Call the method to test
        Review createdReview = reviewService.createReview(reviewRequest, user);

        // Verify results
        assertEquals("Great product!", createdReview.getReview());
        assertEquals(user, createdReview.getUser());
        assertEquals(product, createdReview.getProduct());
    }

    @Test
    void testCreateReview_ProductNotFound() throws ProductException {
        User user = new User();
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setProductId(1L);
        reviewRequest.setReview("Good quality!");

        // Mocking the behavior
        when(productService.findProductById(1L)).thenThrow(new ProductException("Product not found"));

        // Assert that an exception is thrown
        assertThrows(ProductException.class, () -> {
            reviewService.createReview(reviewRequest, user);
        });
    }

    @Test
    void testGetAllReviews() {
        // Prepare mock data
        Product product = new Product();
        product.setId(1L);

        Review review1 = new Review();
        review1.setReview("Excellent!");

        Review review2 = new Review();
        review2.setReview("Not bad!");

        List<Review> reviews = List.of(review1, review2);

        // Mock the repository method
        when(reviewRepository.getAllProductsReview(1L)).thenReturn(reviews);

        // Call the method to test
        List<Review> fetchedReviews = reviewService.getAllReview(1L);

        // Verify results
        assertEquals(2, fetchedReviews.size());
        assertEquals("Excellent!", fetchedReviews.get(0).getReview());
        assertEquals("Not bad!", fetchedReviews.get(1).getReview());
    }
}
