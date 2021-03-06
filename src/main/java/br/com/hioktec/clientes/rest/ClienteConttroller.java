package br.com.hioktec.clientes.rest;

import br.com.hioktec.clientes.model.entity.Cliente;
import br.com.hioktec.clientes.model.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/clientes")
public class ClienteConttroller {

    private final ClienteRepository repository;
    
    private final MessageSource messageSource;

    @Autowired
    public ClienteConttroller( ClienteRepository repository, MessageSource messageSource ) {
        this.repository = repository;
        this.messageSource = messageSource;
    }

    @GetMapping
    public List<Cliente> obterTodos() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // para mudar o response para CREATED ao invés do padrão OK
    public Cliente salvar( @Valid @RequestBody Cliente cliente ){
    	if(repository.findByCpf(cliente.getCpf()).isPresent()) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
    						messageSource.getMessage("response.cpf.existente", null, Locale.getDefault()));
    	}
        return repository.save(cliente);
    }

    @GetMapping("{id}")
    public Cliente buscarPorId( @PathVariable Long id ){
       return repository
               .findById(id)
               .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
										messageSource.getMessage("response.cliente.inexistente",
															null, Locale.getDefault())));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // requisição OK mas sem resurso de retorno
    public void deletar( @PathVariable  Long id ){
        repository
                .findById(id)
                .map(cliente -> {
                    repository.delete(cliente);
                    return Void.TYPE;
                })
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                						messageSource.getMessage("response.cliente.inexistente", 
                											null, Locale.getDefault())));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar( @PathVariable Long id, @Valid @RequestBody Cliente clienteAtualizado){
        repository
                .findById(id)
                .map(cliente -> {
                    cliente.setNome(clienteAtualizado.getNome());
                    cliente.setCpf(clienteAtualizado.getCpf());
                    return repository.save(cliente);
                })
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                						messageSource.getMessage("response.cliente.inexistente", 
                											null, Locale.getDefault())));
    }
}
