package com.spring.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.spring.boot.service.MyAuthenticationSuccessHandler;
import com.spring.boot.service.MyUserDetailsService;


@EnableWebSecurity
@Configuration
public class MyConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder(12);
	}
	@Autowired
    private MyUserDetailsService myUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(customizer -> customizer.disable());
        
        http.formLogin(login -> login
                .loginPage("/signin") // Set the custom login page URL
                .successHandler(new MyAuthenticationSuccessHandler()) // Set custom handler
                .permitAll()
        		);
        
        http.authorizeHttpRequests(request -> request
        		.requestMatchers("/css/**", "/js/**", "/img/**").permitAll() 
                .requestMatchers("/", "signup", "login", "signin", "about", "doRegister").permitAll()
                .requestMatchers("/user/**").hasRole("USER")  // Restrict to USER role
                .requestMatchers("/admin/**").hasRole("ADMIN") // Restrict to ADMIN role
                .anyRequest().authenticated());
        
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return daoAuthenticationProvider;
    }
}
