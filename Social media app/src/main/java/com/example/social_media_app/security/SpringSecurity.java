package com.example.social_media_app.security;


import org.modelmapper.ModelMapper;
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

import static org.springframework.security.config.Customizer.withDefaults;

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
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .logout(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth->
//                        auth.requestMatchers("/login",
//                                        "/users/signup").permitAll()
//                                .requestMatchers(HttpMethod.GET).hasAnyRole("USER","ADMIN")
//                                .requestMatchers(HttpMethod.POST).hasAnyRole("ADMIN")
//                                .requestMatchers(HttpMethod.PUT).hasAnyRole("ADMIN")
//                                .requestMatchers(HttpMethod.PATCH).hasAnyRole("ADMIN")
//                                .requestMatchers(HttpMethod.DELETE).hasAnyRole("ADMIN")
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

//        http.csrf(AbstractHttpConfigurer::disable)
//                // .cors()
//                //.and()
//                .authorizeRequests(authorize ->
//                                authorize
//                                        .requestMatchers("users/signup","/logout","/favicon.ico", "/css/**", "/js/**", "/images/**").permitAll()
//                                       //.requestMatchers(HttpMethod.GET).hasAnyAuthority("USER","ADMIN")
//                                        .requestMatchers(HttpMethod.PATCH, "/users/{id}", "/posts/updatePost/{id}", "/comments/{id}").hasAnyAuthority("USER", "ADMIN")
//                                        .requestMatchers(HttpMethod.DELETE, "/users/{id}", "/posts/deletePost/{id}", "/comments/{id}", "/likes/{id}").hasAnyAuthority("USER", "ADMIN")
////                                        .requestMatchers("/users/admin", "/users/admin/**",
////                                                "/admin/updateUser/**","/admin/deleteUser/**","admin/deletePost/**","/posts/allPosts"
////                                        ,"/posts/findPost/**","/posts/user/**","/comments/**","/comments/post/**")
////                                        .hasAuthority("ADMIN")
////                                        .requestMatchers("/posts","/posts/updatePost/**","/posts/deletePost/**","/posts/my-posts","/comments"
////                                        ,"/comments/updateComment/**","/comments/deleteComment/**","/likes/**","/likes/remove/**","/likes/post/**")
////                                        .hasAnyAuthority("USER","ADMIN")
//                                        .anyRequest().authenticated())
//                .formLogin(withDefaults());
//
//
        http.csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorize ->
                        authorize
                                // Public endpoints
                                .requestMatchers(HttpMethod.POST, "/users/signup").permitAll()
                                .requestMatchers("/logout").permitAll()
                                .requestMatchers("/favicon.ico", "/css/*", "/js/", "/images/*").permitAll()

                                // User and Admin endpoints
                                .requestMatchers(HttpMethod.GET, "/users/{id}","/posts", "/posts/{id}", "/posts/user/{userId}","/posts/my-posts","/","/users/search/{username}","/users/{id}/profile").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/comments/{id}", "/comments/post/{postId}").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/likes/post/{postId}","/likes/count/{postId}","/likes//users/{postId}").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/follow/followers/{userId}","/follow/following/{userId}","/follow/check/{userId}").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/posts", "/comments", "/likes/{postId}").hasAnyAuthority("USER", "ADMIN")


                                // Admin-only endpoints
                                .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ADMIN")

                                // Endpoints that require custom authorization in the controller
                                .requestMatchers(HttpMethod.PATCH, "/users/{id}", "/posts/{id}", "/comments/{id}").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/{id}", "/posts/{id}", "/comments/{id}", "/likes/{id}","/follow/{userId}").hasAnyAuthority("USER", "ADMIN")

                                .anyRequest().authenticated())
                .formLogin(withDefaults());

       return http.build();
    }

//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                // .cors()
//                //.and()
//                .authorizeRequests(authorize ->
//                        authorize.requestMatchers("/login",
//                                       "/users/signup").permitAll()
//                                .requestMatchers(HttpMethod.GET).hasAnyRole("USER","ADMIN")
//                               .requestMatchers(HttpMethod.POST).hasAnyRole("ADMIN")
//                               .requestMatchers(HttpMethod.PUT).hasAnyRole("ADMIN")
//                               .requestMatchers(HttpMethod.PATCH).hasAnyRole("ADMIN")
//                                .requestMatchers(HttpMethod.DELETE).hasAnyRole("ADMIN")
//                               .anyRequest().authenticated())
//                .formLogin(withDefaults());
//
//
//        return http.build();
//    }


}



