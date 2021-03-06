package br.com.hioktec.clientes.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.hioktec.clientes.model.entity.Cliente;
import br.com.hioktec.clientes.model.entity.ServicoPrestado;
import br.com.hioktec.clientes.model.repository.ClienteRepository;
import br.com.hioktec.clientes.model.repository.ServicoPrestadoRepository;
import br.com.hioktec.clientes.rest.dto.ServicoPrestadoDTO;
import br.com.hioktec.clientes.util.BigDecimalConverter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/servicos-prestados")
@RequiredArgsConstructor // Cria construtor com os arqumentos requeridos (final). O Spring faz a injeção automaticamente via construtor.
public class ServicoPrestadoController {
	
	private final ClienteRepository clienteRepository;
	private final ServicoPrestadoRepository repository;
	private final BigDecimalConverter bigDecimalConverter;
	private final MessageSource messageSource;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED) // para dar created ao invés do padrao ok
	public ServicoPrestado salvar(@Valid @RequestBody ServicoPrestadoDTO dto) {
		LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		BigDecimal valor = bigDecimalConverter.converter(dto.getPreco());
		Long idCliente = dto.getIdCliente();
		Cliente cliente= clienteRepository
				.findById(idCliente)
				.orElseThrow(() -> 
					new ResponseStatusException(HttpStatus.BAD_REQUEST,
							messageSource.getMessage("response.cliente.inexistente", null, Locale.getDefault())));
				
		
		ServicoPrestado servicoPrestado = new ServicoPrestado();
		servicoPrestado.setDescricao(dto.getDescricao());
		servicoPrestado.setData(data);
		servicoPrestado.setValor(valor);
		servicoPrestado.setCliente(cliente);
		
		return repository.save(servicoPrestado);
	}
	
	@GetMapping
	public List<ServicoPrestado> pesquisar(
			@RequestParam(value = "nome", required = false, defaultValue = "") String nome,
			@RequestParam(value = "mes", required = false) Integer mes){
		if(mes == null) {
			return repository.findByNomeCliente( "%" + nome + "%");
		}
		return repository.findByNomeClienteAndMes( "%" + nome + "%", mes); // % para procurar como contém 
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		ServicoPrestado servico = repository.findById(id)
									.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
											messageSource.getMessage("response.servico.inexistente", null, Locale.getDefault())));
		repository.delete(servico);
	}
	
	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizar(@PathVariable Long id, @Valid @RequestBody ServicoPrestadoDTO dto) {
		Optional<ServicoPrestado> servicoOptional = repository.findById(id);
		if(servicoOptional.isPresent()) {
			LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			BigDecimal valor = bigDecimalConverter.converter(dto.getPreco());
			Cliente cliente = clienteRepository.findById(dto.getIdCliente())
								.orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,
									messageSource.getMessage("response.cliente.inexistente", null, Locale.getDefault())));
			
			ServicoPrestado servico = servicoOptional.get();
			servico.setDescricao(dto.getDescricao());
			servico.setData(data);
			servico.setValor(valor);
			servico.setCliente(cliente);
			
			repository.save(servico);
		}
	}
	
	@GetMapping("{id}")
	public ServicoPrestado buscarPorId(@PathVariable Long id) {
		return repository
					.findById(id)
					.orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,
							messageSource.getMessage("response.servico.inexistente", null, Locale.getDefault())));
	}
}
