package com.nelioalves.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private Environment env;
	
	private static final String[] PUBLILC_MATCHERS = {
			"/h2-console/**"
	};
	
	private static final String[] PUBLILC_MATCHERS_GET = {
			"/categorias/**",
			"/produtos/**",
			"/clientes/**",
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//Necessário para o funcionamento do h2-console
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.cors().and()
			//Desabilita segurança CSRF, uma vez que a aplicação é stateless, não será necessário
			.csrf().disable();
		http.authorizeRequests()
			.antMatchers(PUBLILC_MATCHERS).permitAll()
			//Define que somente os métodos get estão liberados para essas requisições
			.antMatchers(HttpMethod.GET,PUBLILC_MATCHERS_GET).permitAll()
			//Para qualquer outra requisição que não foi explicitamente permitida, exigir autenticação
			.anyRequest().authenticated();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
