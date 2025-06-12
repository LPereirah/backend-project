package com.vidaplus.sghss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Define an in-memory user for basic testing
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Create a user with a specific role (e.g., "ADMIN")
        UserDetails admin = User.builder()
                .username("admin") // You can use "admin" instead of "user" for clarity
                .password(passwordEncoder.encode("adminpass")) // Encode a default password
                .roles("ADMIN") // Assign the "ADMIN" role
                .build();

        // If you still want the "user" with the generated password (for quick basic auth),
        // you would typically configure it differently or just rely on the above "admin" user.
        // For simplicity and direct access for all GETs, let's use this "admin" user.

        return new InMemoryUserDetailsManager(admin);
    }

    //Method to make some tests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity in API testing
                .authorizeHttpRequests(authorize -> authorize
                        // Permit all requests to signup and login endpoints for all user types
                        .requestMatchers("/patients/signup", "/patients/login").permitAll()
                        .requestMatchers("/adm-users/signup", "/adm-users/login").permitAll()
                        .requestMatchers("/adm-hospital/signup", "/adm-hospital/login").permitAll()
                        .requestMatchers("/hc-professional/signup", "/hc-professional/login").permitAll()

                        // Permit all GET requests to /api/** for users with the "ADMIN" role
                        // This means 'admin' user can perform GETs to any of your API resources
                        .requestMatchers("/vidaplus/**").hasRole("ADMIN") // Allow ADMIN role to access all /api paths

                        // All other requests that are not explicitly permitted above (e.g., POST/PUT/DELETE
                        // to general API paths if not done by ADMIN) require authentication.
                        // For now, this will imply a 403 unless the 'admin' role has higher permissions
                        // or if we add specific authentication for your custom users.
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults()); // Enable HTTP Basic authentication

        return http.build();
    }

}
