package br.com.progic.desafio.exception;

public class NaoEncontradoException extends RuntimeException {

    public NaoEncontradoException(final String message) {
        super(message);
    }
}
