package com.decafmango.lab_1.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", unique = true, nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id", nullable = false)
//    private List<Car> cars;
//
//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id", nullable = false)
//    private List<Coordinates> coordinates;
//
//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id", nullable = false)
//    private List<HumanBeing> humanBeings;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

}
