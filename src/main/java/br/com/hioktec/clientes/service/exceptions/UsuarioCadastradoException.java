package br.com.hioktec.clientes.service.exceptions;

public class UsuarioCadastradoException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public UsuarioCadastradoException(String mensagem) {
		super(mensagem);
	}
}
