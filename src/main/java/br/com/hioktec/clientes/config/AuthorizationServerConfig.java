package br.com.hioktec.clientes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Classe que habilita o servidor de autenticação (Oauth2)
 * e configura o mesmo.
 * @author rodolfo
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
	
	/**
	 * Injeção da dependância de AuthenticationManager gerenciada pelo spring.
	 * E é instanciada conforme configuramos na classe SecurityConfig.
	 */
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/**
	 * Injeção da dependância de PasswordEncoder gerenciada pelo spring.
	 * E é instanciada conforme configuramos na classe SecurityConfig.
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * Campo é carregado com o valor de security.jwt.signing-key no arquivo application.properties
	 */
	@Value("${security.jwt.signing-key}")
	private String signingkey;
	
	/**
	 * Cria método disponivel para toda a aplicação spring:
	 * @return que retorna uma instância de JwtAccessTokenConverter configurado com a signingkey (chave de segurança)
	 * (conversor de token de acesso do tipo JWT) 
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(signingkey);
		return tokenConverter;
	}
	
	/**
	 * Cria método disponivel para toda a aplicação spring:
	 * @return que cria uma instância de TokenStore do tipo JwtTokenStore
	 * (armazenamento de token do tipo JWT)
	 */
	@Bean
	public TokenStore tokenStore() {
		// return new InMemoryTokenStore(); somente para teste.
		return new JwtTokenStore(jwtAccessTokenConverter());
	}
	
	/**
	 * Configuração do endpoints do servidor de autenticação:
	 * configurando o tokenStore e o jwtAccessTokenConverter criados pelos beans acima
	 * e também o authenticationManager injetado da classe SecurityConfig.
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
				.tokenStore(tokenStore())
				.accessTokenConverter(jwtAccessTokenConverter())
				.authenticationManager(authenticationManager);
	}
	
	/**
	 * Configuração das aplicações clientes, no caso nossa aplicação Angular do Front-End:
	 * informando onde estão/estarão os dados, o id do cliente, a senha de acesso do cliente com o decodificador da senha,
	 * o escopo de permissões (no caso de leitura e escrita), o tipo de autorização e a validade do token em segundos,
	 * respectivamente.
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
			.inMemory()
			.withClient("my-angular-app")
			.secret(passwordEncoder.encode("@321"))
			.scopes("read", "write")
			.authorizedGrantTypes("password")
			.accessTokenValiditySeconds(1800);
	}
}
