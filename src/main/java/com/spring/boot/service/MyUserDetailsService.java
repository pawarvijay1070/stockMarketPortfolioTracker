package com.spring.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.boot.entities.User;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getUserByEmail(username);

        if (user == null)
        {
            System.out.println(username+ " - User not found");
            throw new UsernameNotFoundException("User not found.!");
        }

        return new MyUserDetails(user);
	}

}
