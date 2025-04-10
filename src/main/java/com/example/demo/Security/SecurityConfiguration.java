package com.example.demo.Security;

import com.example.demo.Security.jwt.JwtRequestFilter;
import com.example.demo.Security.jwt.UnauthorizedHandlerJwt;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    //admin username and password to the application.properties file
    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService inMemoryUserDetailsService() {
        UserDetails admin = User.builder()
                .username(adminUsername)
                .password(adminPassword)
                .roles("ADMIN")
                .build();

        System.out.println("ADMIN precargado: " + admin.getUsername() + " / " + admin.getPassword());
        return new InMemoryUserDetailsManager(admin);
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .securityMatcher("/api/**")
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PRIVATE ENDPOINTS
                        .requestMatchers(HttpMethod.GET, "/api/users/me").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,"/api/products/").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/products/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,"/api/products/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/categories/**").hasRole("ADMIN") //aunque creo que no hay put
                        .requestMatchers(HttpMethod.DELETE,"/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/").hasRole("USER")
                        // PUBLIC ENDPOINTS
                        .anyRequest().permitAll()
                );

        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT Token filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PUBLIC PAGES
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/images/public/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/loginerror").permitAll()
                        .requestMatchers("/category/**").permitAll()
                        .requestMatchers("/products").permitAll()
                        .requestMatchers("/products/*/image").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/js/**", "/webjars/**", "/favicon.ico").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()


                        // PRIVATE PAGES
                        .requestMatchers("/cart").hasRole("USER")
                        .requestMatchers("/orders/**").hasRole("USER")
                        .requestMatchers("/products/add**").hasRole("ADMIN")
                        .requestMatchers("/users/add").hasRole("ADMIN")
                        .requestMatchers("/categories/*").hasRole("ADMIN")
                        .requestMatchers("/cart/checkout").hasRole("USER")
                        .requestMatchers("/categories/*/delete").hasRole("ADMIN")
                        .requestMatchers("/cart/add/*").hasRole("USER")
                        .requestMatchers("/manageProducts").hasRole("ADMIN")
                        .requestMatchers("/products/manage").hasRole("ADMIN")
                        .requestMatchers("/products/*/delete").hasRole("ADMIN")
                        .requestMatchers("/users/manage").hasRole("ADMIN")
                        .requestMatchers("/users/*/delete").hasRole("ADMIN")
                        .requestMatchers("/products/*/edit").hasRole("ADMIN")
                        .requestMatchers("/products/*/update").hasRole("ADMIN")
                        .requestMatchers("/profile", "/profile/delete").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/**").denyAll()


                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureUrl("/loginerror")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }

}
