package com.project.user_service.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.user_service.entities.enums.Status;
import com.project.user_service.entities.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "user_table",
   indexes = {
   @Index(name = "idx_email",columnList = "email",unique = true)

   }

)

public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true,nullable = false)

    private String FullName;
    private String email;
    private String password;
    private Number phone;

    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    private LocalDateTime createAt;

    private Status status;
    private String verificationToken;

    @JsonProperty
    private boolean email_verified;
    @JsonProperty
    private  boolean profile_complete;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @CurrentTimestamp
    private LocalDateTime verifyTokenExpireAt;

    private LocalDateTime updateAt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role->new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return email;
    }
}
