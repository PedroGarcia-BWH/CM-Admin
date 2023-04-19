package es.uca.cm.admin.security;

import es.uca.cm.admin.webUser.WebUser;
import es.uca.cm.admin.webUser.WebUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final WebUserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(WebUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WebUser user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPass(),
                    getAuthorities(user));
        }
    }

    private List<GrantedAuthority> getAuthorities(WebUser user) {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority("ROLE_" ));
        //System.out.println("Rol: " + list.get(0));
        return list;
    }

}