package com.project.user_service.dto;

import com.project.user_service.entities.enums.UserRole;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDto {
        private UUID id;
        private String email;
        private String fullName;
        private String phoneNo;
        private UserRole role;
        private boolean email_verified;
        private  boolean profile_complete;
        private LocalDateTime createAt;
}
