package com.example.social_media_app.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
    @Autowired
    private final UserDetailService userDetailService;

    public SpringSecurity(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());
    }


    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .logout(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth->
//                        auth.requestMatchers("/login",
//                                        "/logout",
//                                        "/users/signup").permitAll()
////                                .requestMatchers(HttpMethod.GET).hasAnyRole("ADMIN")
////                                .requestMatchers(HttpMethod.POST).hasAnyRole("ADMIN")
////                                .requestMatchers(HttpMethod.PUT).hasAnyRole("ADMIN")
////                                .requestMatchers(HttpMethod.PATCH).hasAnyRole("ADMIN")
////                                .requestMatchers(HttpMethod.DELETE).hasAnyRole("ADMIN")
//                               .anyRequest().authenticated())
//                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .httpBasic(Customizer.withDefaults())
//        ;
//        return http.build();
//    }

//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .logout(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login", "/logout", "/users/signup" , "/users").permitAll()
////                        .requestMatchers(HttpMethod.GET, "/**").hasAnyRole("ADMIN", "USER")
////                        .requestMatchers(HttpMethod.POST, "/**").hasAnyRole("ADMIN", "USER")
////                        .requestMatchers(HttpMethod.PATCH, "/**").hasAnyRole("ADMIN", "USER")
////                        .requestMatchers(HttpMethod.DELETE, "/**").hasAnyRole("ADMIN", "USER")
//                        .anyRequest().authenticated())
//                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .httpBasic(Customizer.withDefaults());
//        return http.build();
//    }


    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                // .cors()
                //.and()
                .authorizeRequests(authorize ->
                                authorize
                                        .requestMatchers("/users/signup").permitAll()
                                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }



}
