package br.com.hioktec.clientes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Classe que habilita o Servidor de recursos (do Oauth2) 
 * e configura segurança as requisições de recursos.
 * @author rodolfo
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests() // autorização de requisições
				.antMatchers("/api/usuarios").permitAll() // para api/usuarios permite/autoriza a todos.
				.antMatchers("/api/clientes/**",          // já para clientes e acima somente os autenticados.
							 "/api/servicos-prestados/**").authenticated() // para servicos-prestados e acima, iden.
				.anyRequest().denyAll();				  // para outras requisições recusa todas. 
	}
}
