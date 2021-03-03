package br.com.hioktec.clientes.model.repository;

import br.com.hioktec.clientes.model.entity.ServicoPrestado;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServicoPrestadoRepository extends JpaRepository<ServicoPrestado, Long> {

	@Query("select s from ServicoPrestado s join s.cliente c "
			+ "where upper(c.nome) like upper(:nome) "
			+ "and month(s.data) = :mes")
	List<ServicoPrestado> findByNomeClienteAndMes(
			@Param("nome") String nome,
			@Param("mes") Integer mes);

	@Query("select s from ServicoPrestado s join s.cliente c "
			+ "where upper(c.nome) like upper(:nome)")
	List<ServicoPrestado> findByNomeCliente(@Param("nome") String string);

}
