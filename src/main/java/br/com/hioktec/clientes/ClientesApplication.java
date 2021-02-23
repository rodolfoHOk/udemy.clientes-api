package br.com.hioktec.clientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* somente para teste de conexão com o db
 import br.com.hioktec.clientes.model.entity.Cliente;
 import br.com.hioktec.clientes.model.repository.ClienteRepository;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.CommandLineRunner;
 import org.springframework.context.annotation.Bean;
*/

@SpringBootApplication // anotação esta classe inicia a aplicação
public class ClientesApplication {

    /* teste de conexao com o banco de dados? OK
    @Bean
    public CommandLineRunner run( @Autowired ClienteRepository repository ){
        return args -> {
            Cliente cliente = Cliente.builder().cpf("12345678901").nome("Fulano").build();
            repository.save(cliente);
        };
    }
    */

    public static void main(String[] args) {
        SpringApplication.run(ClientesApplication.class, args);
    }
}
