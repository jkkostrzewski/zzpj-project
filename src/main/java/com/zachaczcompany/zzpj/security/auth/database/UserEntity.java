package com.zachaczcompany.zzpj.security.auth.database;

import com.zachaczcompany.zzpj.security.auth.User;
import com.zachaczcompany.zzpj.security.configuration.UserRole;
import com.zachaczcompany.zzpj.shops.domain.Shop;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @OneToOne
    private Shop shop;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> permissions;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLock;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    public UserEntity(String username, String password, UserRole userRole, Shop shop) {
        this.username = username;
        this.password = password;
        this.permissions = getPermissions(userRole.getGrantedAuthority());
        this.shop = shop;
        this.isAccountNonExpired = true;
        this.isAccountNonLock = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }

    public User getApplicationUser() {
        return new User(username, password, getGrantedAuthority(), isAccountNonExpired,
                isAccountNonLock, isCredentialsNonExpired, isEnabled);
    }

    private Set<SimpleGrantedAuthority> getGrantedAuthority() {
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private Set<String> getPermissions(Collection<? extends GrantedAuthority> grantedAuthority) {
        return grantedAuthority.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
