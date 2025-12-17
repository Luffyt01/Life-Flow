package com.project.user_service.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name= "forgetTable")
public class ForgetPasswordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String forget_token;

    // This is option
//    @OneToOne
//    private UserEntity user;

//    @Column(name = "user_id")
//    private Long userId;

    @Column(unique = true)
    private String email;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @CreationTimestamp
    private LocalDateTime expiredAt;

    public ForgetPasswordEntity() {

    }
}
