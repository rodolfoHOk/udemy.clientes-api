package br.com.hioktec.clientes.rest;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.hioktec.clientes.model.entity.Usuario;
import br.com.hioktec.clientes.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
	
	private final UsuarioRepository repository;
	
	private final PasswordEncoder passwordEncoder;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void salvar( @Valid @RequestBody Usuario usuario) {
		if(repository.findByUsername(usuario.getUsername()).isPresent() ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome de usuário informado já existe!");
		} else if(repository.findByEmail(usuario.getEmail()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail informado já existe!");
		} else {
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
			repository.save(usuario);
		}
	}
	
}
