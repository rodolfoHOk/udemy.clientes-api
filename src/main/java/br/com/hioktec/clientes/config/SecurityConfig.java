package br.com.hioktec.clientes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.hioktec.clientes.service.UsuarioService;

/* Somente para teste do Oauth2
@EnableWebSecurity
@EnableResourceServer
@EnableAuthorizationServer
public class SecurityConfig extends WebSecurityConfigurerAdapter {
}
/*

/**
 * Classe habilita e configura a segurança da aplicação.
 * @author rodolfo
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UsuarioService usuarioService;
	
	/**
	 * Cria método disponivel para toda a aplicação spring:
	 * @return que retorna uma instância de passwordEncoder do tipo BCryptPasswordEncoder;
	 * passwordEncoder serve para codificar e decodificar, neste caso, as senhas guardadas no banco de dados.
	 */
	@Bean 
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 *  Configurar o builder para o AuthenticationManager:
	 *  1o) Provedor de usuário (userDetailsService)
	 *  2o) Decodificador da senha (passwordEncoder)
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/* em memória para teste somente 
		auth.inMemoryAuthentication()
				.withUser("fulano")
				.password("1234")
				.roles("USER");
		*/
		auth
			.userDetailsService(usuarioService)
			.passwordEncoder(passwordEncoder());
	}
	
	/**
	 * Cria método disponivel para toda a aplicação spring:
	 * @return que retorna uma instância de AuthenticationManager com base no builder configurado acima.
	 */
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	/**
	 * Configura as seguranças de requisições http.
	 * Nesta configuração só é configurada a para de segurança da aplicação.
	 * Configuramos a segurança de http referentes as requisições de recursos em outra classe (ResourceServerConfig).
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // aplicação web é separada (Aplicação Angular) e usaremos o Oauth2, então desabilitamos
			.cors().and() // habilita o cors que configuramos na classe WebConfig. and() sai do cors e volta para httpSecurity
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // não usarenos sessões,
										// ao invés, usaremos um token com tempo de expiração.
	}
	
	
}
