package br.com.hioktec.clientes.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.hioktec.clientes.model.entity.Usuario;
import br.com.hioktec.clientes.model.repository.UsuarioRepository;
import br.com.hioktec.clientes.service.exceptions.UsuarioCadastradoException;

/**
 * Serviço que nos disponibilizará o usuário para o gerenciador de autenticação (AuthenticationManager)
 * @author rodolfo
 */
@Service
public class UsuarioService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MessageSource messageSource;
	
	public Usuario salvar(Usuario usuario) {
		boolean existeNomeUsuario = repository.existsByUsername(usuario.getUsername());
		if(existeNomeUsuario) {
			String mensagem = messageSource.getMessage("exception.username.existente", null, Locale.getDefault()) 
								+ " " + usuario.getUsername();
			throw new UsuarioCadastradoException(mensagem);
		}
		
		boolean existeEmail = repository.existsByEmail(usuario.getEmail());
		if(existeEmail) {
			String mensagem = messageSource.getMessage("exception.email.existente", null, Locale.getDefault()) 
					+ " " + usuario.getEmail();
			throw new UsuarioCadastradoException(mensagem);
		}
		
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		return repository.save(usuario);
	}

	/**
	 * Carrega o usuário pelo seu nome se usuário (username), fazendo a busca no banco de dados.
	 * @return e retornado uma instância de User (UserDetails)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = repository
							.findByUsername(username)
							.orElseThrow(()-> new UsernameNotFoundException(
														"Nome de usuário informado não encontrado!"));
		
		return User
				.builder()
				.username(usuario.getUsername())
				.password(usuario.getPassword())
				.roles("USER")
				.build();
	}
	
}
