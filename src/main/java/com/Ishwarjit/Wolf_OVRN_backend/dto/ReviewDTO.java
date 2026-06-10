package com.Ishwarjit.Wolf_OVRN_backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Location cannot be blank")
    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Short rating;

    @NotBlank(message = "Text cannot be blank")
    private String text;

    @NotBlank(message = "Product cannot be blank")
    @Size(max = 100, message = "Product must not exceed 100 characters")
    private String product;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @Size(max = 500, message = "Link must not exceed 500 characters")
    private String link;

    private Boolean isActive;
}
