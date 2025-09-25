package br.com.progic.desafio.controller;

import br.com.progic.desafio.exception.ConteudoInvalidoException;
import br.com.progic.desafio.exception.ErroResponse;
import br.com.progic.desafio.exception.NaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GenericoExceptionHandler {

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontradoException(final NaoEncontradoException exception) {
        return new ResponseEntity<>(
                new ErroResponse.Builder()
                        .httpStatus(HttpStatus.NOT_FOUND.value())
                        .mensagem(exception.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ConteudoInvalidoException.class)
    public ResponseEntity<ErroResponse> conteudoInvalidoException(final ConteudoInvalidoException exception) {
        return new ResponseEntity<>(
                new ErroResponse.Builder()
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .mensagem(exception.getMessage())
                        .detalhes(exception.getDetalhes())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> methodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final var erros = new HashMap<String, String>();
        exception.getBindingResult().getFieldErrors().forEach(e -> erros.put(e.getField(), e.getDefaultMessage()));
        return new ResponseEntity<>(
                new ErroResponse.Builder()
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .mensagem("dado(s) invalido(s)")
                        .detalhes(erros)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
