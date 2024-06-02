package com.dawes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.dawes.servicioImpl.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Bean
	public UserDetailsServiceImpl userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// DaoAuthenticationProvider es una implementacion del AuthenticationProvider
	// Se utiliza comunmente para autenticar usuarios en bases de datos
	// Es responsable de verificar las credenciales del usuario y autenticar el
	// usuario
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity
				.getSharedObject(AuthenticationManagerBuilder.class);
		// cuando se realiza una solicitud el administrador de autenticación usará el
		// provider
		authenticationManagerBuilder.authenticationProvider(authenticationProvider());
		return authenticationManagerBuilder.build();
	}

	// Define las reglas de autorizacion para las solicitudes HTTP
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

		
		httpSecurity
		    .authorizeHttpRequests(authorizeRequests -> {
		        // Páginas públicas
		        authorizeRequests
		            .requestMatchers("/", "/index", "/header", "/footer", "/hoteles", "/habitaciones", "/contacto", "/enviarcomentario", "/mensajeenviado", "/login", "/addUsuario", "/error", "/errordisponiblereserva")
		            .permitAll()
		            // Permit access to static resources
		            .requestMatchers("/bs/**", "/css/**", "/font/**", "/img/**", "/js/**").permitAll()
		            // Rol admin
		            .requestMatchers("/administracion/**").hasRole("ADMIN")
		            // Rol staff
		            .requestMatchers("/recepcion/**").hasAnyRole("STAFF", "ADMIN")
		            // Roles admin staff user
		            .requestMatchers("/registrado/**").hasAnyRole("USER", "STAFF", "ADMIN")
		            .anyRequest().authenticated();
		    })
		    
		    // Usuario no autorizado registrado en bbdd pero sin permisos necesarios
		    .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedPage("/403"))
		    
		    // Página de login
		    .formLogin(formLogin -> formLogin
		        .loginProcessingUrl("/j_spring_security_check") // Submit URL
		        .loginPage("/login")
		        .failureUrl("/login?error=true")
		        .usernameParameter("username")
		        .passwordParameter("password"))
		    
		    .logout(logout -> logout
//		        .logoutUrl("/logout")
		        .logoutSuccessUrl("/"))
		    ;
	
		return httpSecurity.build();

	}
}
