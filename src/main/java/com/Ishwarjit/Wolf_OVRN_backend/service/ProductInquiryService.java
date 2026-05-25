package com.Ishwarjit.Wolf_OVRN_backend.service;

import com.Ishwarjit.Wolf_OVRN_backend.dto.InquiryRequest;
import com.Ishwarjit.Wolf_OVRN_backend.dto.InquiryResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.InquiryStatusUpdateRequest;
import com.Ishwarjit.Wolf_OVRN_backend.entity.Product;
import com.Ishwarjit.Wolf_OVRN_backend.entity.ProductInquiry;
import com.Ishwarjit.Wolf_OVRN_backend.entity.User;
import com.Ishwarjit.Wolf_OVRN_backend.exception.ResourceNotFoundException;
import com.Ishwarjit.Wolf_OVRN_backend.repository.ProductInquiryRepository;
import com.Ishwarjit.Wolf_OVRN_backend.repository.ProductRepository;
import com.Ishwarjit.Wolf_OVRN_backend.repository.UserRepository;
import com.Ishwarjit.Wolf_OVRN_backend.dto.UserInquiriesGroupResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.UserResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductInquiryService {

    private final ProductInquiryRepository inquiryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductInquiryService(ProductInquiryRepository inquiryRepository, 
                                 ProductRepository productRepository, 
                                 UserRepository userRepository) {
        this.inquiryRepository = inquiryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public InquiryResponse createInquiry(UUID userId, InquiryRequest request) {
        if (inquiryRepository.existsByUserIdAndProductId(userId, request.getProductId())) {
            throw new IllegalArgumentException("You have already submitted an inquiry for this product");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (Boolean.TRUE.equals(product.getInStock())) {
            throw new IllegalArgumentException("Cannot submit inquiry for a product that is currently in stock");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProductInquiry inquiry = new ProductInquiry();
        inquiry.setUser(user);
        inquiry.setProduct(product);
        // Default status is PENDING

        ProductInquiry saved = inquiryRepository.save(inquiry);
        return new InquiryResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<InquiryResponse> getAllInquiries(Pageable pageable) {
        return inquiryRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(InquiryResponse::new);
    }

    @Transactional(readOnly = true)
    public List<UserInquiriesGroupResponse> getGroupedInquiries() {
        List<ProductInquiry> allInquiries = inquiryRepository.findAllByOrderByCreatedAtDesc();
        
        Map<User, List<ProductInquiry>> grouped = allInquiries.stream()
            .collect(Collectors.groupingBy(ProductInquiry::getUser));
            
        return grouped.entrySet().stream()
            .map(entry -> new UserInquiriesGroupResponse(
                UserResponse.from(entry.getKey()),
                entry.getValue().stream().map(InquiryResponse::new).collect(Collectors.toList())
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public InquiryResponse updateInquiryStatus(UUID inquiryId, InquiryStatusUpdateRequest request) {
        ProductInquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry not found"));
        
        inquiry.setStatus(request.getStatus());
        
        ProductInquiry saved = inquiryRepository.save(inquiry);
        return new InquiryResponse(saved);
    }

    @Transactional
    public void deleteInquiry(UUID inquiryId) {
        if (!inquiryRepository.existsById(inquiryId)) {
            throw new ResourceNotFoundException("Inquiry not found");
        }
        inquiryRepository.deleteById(inquiryId);
    }
}
