package security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private String name;
    private String password;

    private Collection<? extends GrantedAuthority> _grantedAuthority = null;

    public UserDetailsImpl(String name, String password, Collection<? extends GrantedAuthority> _grantedAuthority) {
        this.name = name;
        this.password = password;
        this._grantedAuthority = _grantedAuthority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this._grantedAuthority;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

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
}
