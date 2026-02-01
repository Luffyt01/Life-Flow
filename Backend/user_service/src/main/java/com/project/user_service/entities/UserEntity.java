    package com.project.user_service.entities;

    import com.fasterxml.jackson.annotation.JsonProperty;
    import com.project.user_service.entities.enums.AuthProviderType;
    import com.project.user_service.entities.enums.Status;
    import com.project.user_service.entities.enums.UserRole;
    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.CreationTimestamp;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.time.LocalDateTime;
    import java.util.*;

    @Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "users",
   indexes = {
   @Index(name = "idx_email",columnList = "email",unique = true)

   }

)

public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true,nullable = false)
    private String email;
    @JoinColumn(unique = true, nullable = false)
    private String fullName;
    private String password;
    private String phoneNo;

//    @ElementCollection(fetch = FetchType.EAGER)

    private LocalDateTime createAt;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String verificationToken;

    @Enumerated(EnumType.STRING)
    private UserRole role;

        @ElementCollection(fetch = FetchType.EAGER)
        @Enumerated(EnumType.STRING)
    private List<AuthProviderType> provider=new ArrayList<>();


    @JsonProperty
    private boolean email_verified;
    @JsonProperty
    private  boolean profile_complete;
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime verifyTokenExpireAt;

    private LocalDateTime updateAt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }
    }
