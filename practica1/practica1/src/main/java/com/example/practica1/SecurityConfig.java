package com.example.practica1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.practica1.jwt.JwtAuthorizationFilter;
import com.example.practica1.servicios.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Bean para encriptar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Proveedor de autenticación que le dice a Spring Security
     * cómo cargar usuarios (UserDetailsServiceImpl) y qué 
     * encriptador usar (passwordEncoder).
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * El AuthenticationManager es necesario para procesar
     * los intentos de autenticación en el endpoint de login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * La cadena de filtros de seguridad principal que define
     * las reglas de acceso (cuáles rutas son públicas y cuáles protegidas).
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Deshabilitar CSRF (Cross-Site Request Forgery) para APIs REST
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Definir las reglas de autorización por ruta
            .authorizeHttpRequests(authz -> authz
                // Rutas públicas que no requieren token
                .requestMatchers("/api/usuarios/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll() // Permitir registro
                
                // Todas las demás rutas SÍ requieren autenticación (un token válido)
                .anyRequest().authenticated()
            )
            
            // 3. Configurar la gestión de sesión como STATELESS (sin estado)
            // La API no creará ni usará sesiones HTTP.
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 4. Registrar nuestro proveedor de autenticación
            .authenticationProvider(authenticationProvider())
            
            // 5. Añadir nuestro filtro de JWT antes del filtro estándar de Spring
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
