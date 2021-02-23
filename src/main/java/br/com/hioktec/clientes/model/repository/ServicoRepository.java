package br.com.hioktec.clientes.model.repository;

import br.com.hioktec.clientes.model.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

}
