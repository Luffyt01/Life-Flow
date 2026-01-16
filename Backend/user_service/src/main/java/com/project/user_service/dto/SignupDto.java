package com.project.user_service.dto;

import com.project.user_service.entities.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class SignupDto {
    @NotBlank(message = "FullName is required")
    @Size(min = 2, max = 100, message = "FullName must be grater than 2 and less than 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$",
            message = "Phone number must be 10-15 digits and may include country code")
    private String phoneNo;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 30,
            message = "Password must be between 8-30 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least 1 uppercase, 1 lowercase, 1 digit, and 1 special character")
    private String password;
    @NotNull(message = "User role is required")
    private UserRole role;

}
