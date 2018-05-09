package ru.kpfu.itis.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.persistence.dao.UserRepository;
import ru.kpfu.itis.persistence.model.UserRole;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service("userDetailsService")
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyUserDetailService implements UserDetailsService {

    @Autowired
	private UserRepository userRepository;

    @Autowired
	private PasswordEncoder passwordEncoder;

//    @PostConstruct
//	protected void initialize() {
//		save(new ru.kpfu.itis.persistence.model.User("user@gmail.com", "user", "user", UserRole.ROLE_USER, true));
//		save(new ru.kpfu.itis.persistence.model.User("admin@gmail.com", "admin", "admin", UserRole.ROLE_ADMIN, true));
//	}

	@Transactional
    private ru.kpfu.itis.persistence.model.User save(ru.kpfu.itis.persistence.model.User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            ru.kpfu.itis.persistence.model.User user = userRepository.findOneByEmail(email);
            if(user == null) {
                throw new UsernameNotFoundException("No user found with username: " + email);
            }
            return createUser(user);
    }
	
	public void signin(ru.kpfu.itis.persistence.model.User user) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(user));
	}
	
	private Authentication authenticate(ru.kpfu.itis.persistence.model.User user) {
		return new UsernamePasswordAuthenticationToken(createUser(user), null, Collections.singleton(createAuthority(user)));
	}
	
	private User createUser(ru.kpfu.itis.persistence.model.User user) {
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
		return new User(user.getEmail(), user.getPassword(), user.isEnabled(), accountNonExpired,
                credentialsNonExpired, accountNonLocked, Collections.singleton(createAuthority(user)));
	}

	private GrantedAuthority createAuthority(ru.kpfu.itis.persistence.model.User user) {
		return new SimpleGrantedAuthority(user.getRole().toString());
	}

}
