package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogin implements UserDetails, Serializable {
    int id;
    String userName;
    String password;
    String emailAcount;



//    getAuthorities获取用户包含的权限，返回权限集合，权限是一个继承了GrantedAuthority的对象；
//
//    getPassword和getUsername用于获取密码和用户名；
//
//    isAccountNonExpired方法返回boolean类型，用于判断账户是否未过期，未过期返回true反之返回false；
//
//    isAccountNonLocked方法用于判断账户是否未锁定；
//
//    isCredentialsNonExpired用于判断用户凭证是否没过期，即密码是否未过期；
//
//    isEnabled方法用于判断用户是否可用。
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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
