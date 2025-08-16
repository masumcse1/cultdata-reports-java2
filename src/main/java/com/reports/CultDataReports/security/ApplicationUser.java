package com.reports.CultDataReports.security;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String fullusername;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(name = "email", unique = true, nullable = false)
    private String userEmail;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private int role;


    public ApplicationUser() {
        super();
    }

    public ApplicationUser(String fullusername,
                           @Email(message = "Email should be valid") @NotBlank(message = "Email is required") String userEmail,
                           @NotBlank(message = "Password is required") String password, boolean enabled,int role) {
        super();
        this.fullusername = fullusername;
        this.userEmail = userEmail;
        this.password = password;
        this.enabled = enabled;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Update when roles are added
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override public String getPassword() { return password; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFullusername() {
        return fullusername;
    }

    public void setFullusername(String fullusername) {
        this.fullusername = fullusername;
    }

}
