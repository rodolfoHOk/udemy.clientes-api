package br.com.hioktec.clientes.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "{campo.nome.obrigatorio}")
	@Column(nullable = false, length = 150, name = "nome")
	private String nomeCompleto;
	
	@NotEmpty(message = "{campo.email.obrigatorio}")
	@Email(message = "{campo.email.invalido}")
	@Column(unique = true, nullable = false, length = 100)
	private String email;
	
	@NotEmpty(message = "{campo.username.obrigatorio}")
	@Column(unique = true, nullable = false, length = 30, name = "nome_usuario")
	private String username;
	
	@NotEmpty(message = "{campo.senha.obrigatorio}")
	@Column(nullable = false, length = 150, name = "senha")
	private String password;
}
