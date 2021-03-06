package br.com.hioktec.clientes.rest;

import br.com.hioktec.clientes.rest.exceptions.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

// classe para interceptar os erros de validacao e retornar apenas as mensagens dos erros
// obs: anotacao Valid lanca erro de MethodArgumentNotValidException
// interceptar tambem ResponseStatusException e retornar apenas uma mensagem do erro
@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationErrors(MethodArgumentNotValidException exception){
        BindingResult bindingResult = exception.getBindingResult();
        List<String> messages = bindingResult
                .getAllErrors()
                .stream()
                .map( objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiErrors(messages);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrors> handleResponseStatusException(ResponseStatusException exception){
        String mensagemErro = exception.getReason();
        HttpStatus codigoStatus = exception.getStatus();
        ApiErrors apiErrors = new ApiErrors(mensagemErro);
        return new ResponseEntity<ApiErrors>(apiErrors, codigoStatus);
    }
}
