package com.stego.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stego.domain.User;
import com.stego.repository.UserRepo;

@Service
public class UserServiceImpl implements  UserDetailsService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepository; // Custom repository to fetch user data
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		System.err.println("user"+ user.getName());
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		System.err.println(passwordEncoder.matches("12345", user.getPassword()));
		return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
				.password(user.getPassword()).authorities(new SimpleGrantedAuthority("Adib")).build();
	}

	public void addUser(String username, String password, String... roles) {
		User user = new User();
		user.setName(username);
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}

//    @Override
//    public Authentication authenticate(Authentication authentication) {
//        String email = authentication.getName();
//        String password = (String) authentication.getCredentials();
//
//        // Retrieve the user from the database
//        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            throw new BadCredentialsException("User not found");
//        }
//
//        // Validate the password
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new BadCredentialsException("Invalid password");
//        }
//
//        // Set the roles (authorities) of the user
////        List<GrantedAuthority> authorities = new ArrayList<>();
////        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
//
//        // Return a fully authenticated token
//        return new UsernamePasswordAuthenticationToken(email, password, new ArrayList<GrantedAuthority>());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
}
