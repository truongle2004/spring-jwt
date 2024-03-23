package com.example.todoapp.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // enable method level security and PreAuthorize() method
@AllArgsConstructor
public class SpringSecurityConfig {

    private UserDetailsService userDetailsService;
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // The main reason for using CSRF protection in your Spring Security configuration is
    // to enhance the security of your web application by preventing Cross-Site Request
    // Forgery attacks. CSRF attacks occur when an attacker tricks a user's browser into making
    // a malicious request on a trusted site where the user is authenticated. Without proper CSRF
    // protection, an attacker could perform actions on behalf of an authenticated user without their consent.
    // csrf() method:

//http.csrf() configures CSRF protection.
//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()): Configures the repository for CSRF tokens. In this case, it uses cookies to store and retrieve CSRF tokens.
//By using CookieCsrfTokenRepository, the CSRF token is sent and received as a cookie, and the server expects the token to be included in the request header.
//authorizeRequests() method:
//.authorizeRequests().anyRequest().authenticated(): Specifies that any HTTP request must be authenticated. This ensures that only authenticated users can access the application's resources.
//httpBasic() method:
//.httpBasic(Customizer.withDefaults()): Configures HTTP Basic authentication. This is a simple authentication mechanism where the user's credentials are sent in the request header.
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> {
//                    authorize.requestMatchers(HttpMethod.POST,"/api/**").hasRole("ADMIN");
//                            // all the incoming http post should be accessible by the admin
//
//                    authorize.requestMatchers(HttpMethod.DELETE,"/api/**").hasRole("ADMIN");
//                    authorize.requestMatchers(HttpMethod.GET,"/api/**").hasAnyRole("ADMIN", "USER");
//                    // admin and user can be accessible resource
//                    authorize.requestMatchers(HttpMethod.PATCH,"/api/**").hasAnyRole("ADMIN", "USER");
//                    authorize.requestMatchers(HttpMethod.GET,"/api/**").permitAll();
                    authorize.anyRequest().authenticated();
                }).httpBasic(Customizer.withDefaults());
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails truong = User.builder()
//                .username("truong")
//                .password(passwordEncoder().encode("password"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(truong, admin);
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
