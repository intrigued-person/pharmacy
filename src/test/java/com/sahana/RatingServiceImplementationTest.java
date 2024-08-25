package com.sahana;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sahana.exception.ProductException;
import com.sahana.modal.Product;
import com.sahana.modal.Rating;
import com.sahana.modal.User;
import com.sahana.repository.RatingRepository;
import com.sahana.request.RatingRequest;
import com.sahana.service.ProductService;
import com.sahana.service.RatingServiceImplementation;

public class RatingServiceImplementationTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private RatingServiceImplementation ratingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRating_Success() throws ProductException {
        // Prepare mock data
        Product product = new Product();
        product.setId(1L);

        User user = new User();
        user.setId(1L);

        RatingRequest request = new RatingRequest();
        request.setProductId(1L);
        request.setRating(4);

        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(4);
        rating.setCreatedAt(LocalDateTime.now());

        when(productService.findProductById(1L)).thenReturn(product);
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating createdRating = ratingService.createRating(request, user);

        assertEquals(4, createdRating.getRating());
        assertEquals(1L, createdRating.getProduct().getId());
        assertEquals(1L, createdRating.getUser().getId());
    }

    @Test
    void testCreateRating_ProductNotFound() throws ProductException {
        // Prepare mock data
        User user = new User();
        user.setId(1L);

        RatingRequest request = new RatingRequest();
        request.setProductId(1L);
        request.setRating(4);

        when(productService.findProductById(1L)).thenThrow(new ProductException("Product not found"));

        assertThrows(ProductException.class, () -> {
            ratingService.createRating(request, user);
        });
    }

    @Test
    void testGetProductsRating() {
        Product product = new Product();
        product.setId(1L);

        Rating rating1 = new Rating();
        rating1.setProduct(product);
        rating1.setRating(4);

        Rating rating2 = new Rating();
        rating2.setProduct(product);
        rating2.setRating(5);

        List<Rating> ratings = List.of(rating1, rating2);

        when(ratingRepository.getAllProductsRating(1L)).thenReturn(ratings);

        List<Rating> result = ratingService.getProductsRating(1L);

        assertEquals(2, result.size());
        assertEquals(4, result.get(0).getRating());
        assertEquals(5, result.get(1).getRating());
    }
}

