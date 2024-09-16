package com.nucleusTeq.backend.configuration;


import com.nucleusTeq.backend.jwt.AuthEntryPointJwt;
import com.nucleusTeq.backend.jwt.AuthTokenFilter;
import com.nucleusTeq.backend.services.Impl.UsersServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public  class SecurityConfig  {


    @Autowired
    private  UsersServiceImp usersServiceImp;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

   @Bean
   public AuthTokenFilter authenticationJwtTokenFilter(){
       return  new AuthTokenFilter();
   }
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests

                                .requestMatchers("/api/v1/categories/**","/api/v1/issuances/**","/api/v1/users/**","/api/v1/books/**","/api/v1/dashboard/**").hasRole("ADMIN")
                                .requestMatchers("/api/v1/user/**").hasRole("USER")

                                .requestMatchers("/api/v1/**","/error").permitAll()






                                .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session management
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(unauthorizedHandler) // Custom unauthorized handler
                )
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        authProvider.setUserDetailsService(usersServiceImp);

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }


}