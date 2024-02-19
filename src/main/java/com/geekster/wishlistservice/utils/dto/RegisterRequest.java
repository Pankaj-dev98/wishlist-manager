package com.geekster.wishlistservice.utils.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class RegisterRequest {
    @Length(min = 1, max = 128, message = "max characters allowed = 128")
    private String firstName;

    @Length(min = 1, max = 128, message = "max characters allowed = 128")
    private String lastName;

    @Email(message = "Invalid email address")
    private String email;

    private String password;

    @Length(min = 1, max = 128, message = "max characters allowed = 128")
    private String addressLine1;

    @Length(min = 1, max = 128, message = "max characters allowed = 128")
    private String addressLine2;

    private int pinCode;

    @Past
    private LocalDate dateOfBirth;
}