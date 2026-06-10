package com.Ishwarjit.Wolf_OVRN_backend.service;

import com.Ishwarjit.Wolf_OVRN_backend.dto.ReviewDTO;
import com.Ishwarjit.Wolf_OVRN_backend.entity.Review;
import com.Ishwarjit.Wolf_OVRN_backend.exception.ResourceNotFoundException;
import com.Ishwarjit.Wolf_OVRN_backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public List<ReviewDTO> getPublicReviews() {
        return reviewRepository.findByIsActiveTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ReviewDTO> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Transactional
    public ReviewDTO createReview(ReviewDTO dto) {
        Review review = new Review();
        mapToEntity(dto, review);
        if (dto.getIsActive() != null) {
            review.setIsActive(dto.getIsActive());
        }
        Review saved = reviewRepository.save(review);
        return mapToDTO(saved);
    }

    @Transactional
    public ReviewDTO updateReview(Long id, ReviewDTO dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        if (dto.getName() != null) review.setName(dto.getName());
        if (dto.getLocation() != null) review.setLocation(dto.getLocation());
        if (dto.getRating() != null) review.setRating(dto.getRating());
        if (dto.getText() != null) review.setText(dto.getText());
        if (dto.getProduct() != null) review.setProduct(dto.getProduct());
        if (dto.getDate() != null) review.setDate(dto.getDate());
        if (dto.getLink() != null) review.setLink(dto.getLink());
        if (dto.getIsActive() != null) review.setIsActive(dto.getIsActive());

        Review updated = reviewRepository.save(review);
        return mapToDTO(updated);
    }

    @Transactional
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found with id: " + id);
        }
        reviewRepository.deleteById(id);
    }

    private ReviewDTO mapToDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getName(),
                review.getLocation(),
                review.getRating(),
                review.getText(),
                review.getProduct(),
                review.getDate(),
                review.getLink(),
                review.getIsActive()
        );
    }

    private void mapToEntity(ReviewDTO dto, Review review) {
        review.setName(dto.getName());
        review.setLocation(dto.getLocation());
        review.setRating(dto.getRating());
        review.setText(dto.getText());
        review.setProduct(dto.getProduct());
        review.setDate(dto.getDate());
        review.setLink(dto.getLink());
    }
}
