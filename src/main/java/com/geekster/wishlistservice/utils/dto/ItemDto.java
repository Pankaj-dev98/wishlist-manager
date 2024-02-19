package com.geekster.wishlistservice.utils.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ItemDto {
    @NotBlank(message = "Name must not be empty/null")
    private String itemName;

    @Min(value = 1L, message = "Price must be a positive floating point number")
    private double price;

    private Boolean inStock;
}
