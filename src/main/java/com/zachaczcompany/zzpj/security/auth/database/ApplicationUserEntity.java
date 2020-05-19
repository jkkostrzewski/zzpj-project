package com.zachaczcompany.zzpj.security.auth.database;

import com.zachaczcompany.zzpj.security.auth.ApplicationUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
public class ApplicationUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @ElementCollection
    private Set<String> permissions;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLock;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    public ApplicationUserEntity(ApplicationUser applicationUser) {
        this.username = applicationUser.getUsername();
        this.password = applicationUser.getPassword();
        this.permissions = getPermissions(applicationUser.getAuthorities());
        this.isAccountNonExpired = applicationUser.isAccountNonExpired();
        this.isAccountNonLock = applicationUser.isAccountNonLocked();
        this.isCredentialsNonExpired = applicationUser.isCredentialsNonExpired();
        this.isEnabled = applicationUser.isEnabled();
    }

    public ApplicationUser getApplicationUser() {
        return new ApplicationUser(username, password, getGrantedAuthority(),isAccountNonExpired,
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
