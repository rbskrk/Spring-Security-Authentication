package com.mballem.curso.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.UsuarioService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String USUARIO = PerfilTipo.USUARIO.getDesc();

	@Autowired
	private UsuarioService service;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				// acessos públicos liberados
				.antMatchers("/assetsMetronic/**", "/assets/**", "/webjars/**").permitAll()
				
				// acessos privados analista 
				//.antMatchers("/analista/dados").hasAuthority(ANALISTA)
				//.antMatchers("/analista/salvar").hasAuthority(ANALISTA)
				
				
				// acessos privados administrador - PERFIL 5
				//.antMatchers("/u/**").hasAnyAuthority(ADMINISTRADOR)
				//.antMatchers("/unidade/**").hasAnyAuthority(ADMINISTRADOR)
				//.antMatchers("/analista/**").hasAuthority(ADMINISTRADOR)
				
				
				
				/*
				// acessos privados analista e admin
				.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(ANALISTA, ADMIN)
				
				//Acesso a todos logados
				.antMatchers("/contaminantes/analises").hasAnyAuthority(ANALISTA, ADMIN, ADMINFULL)

				 */
				
				.anyRequest().authenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/home", true)
				.failureUrl("/login-error").permitAll().and().logout().logoutSuccessUrl("/").and().exceptionHandling()
				.accessDeniedPage("/acesso-negado").and().rememberMe();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}

}
