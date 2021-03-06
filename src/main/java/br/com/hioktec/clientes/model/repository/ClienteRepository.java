package br.com.hioktec.clientes.model.repository;

import br.com.hioktec.clientes.model.entity.Cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// como extendemos JpaRepository não precisamos anotar como Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	Optional<Cliente> findByCpf(String cpf);
}
