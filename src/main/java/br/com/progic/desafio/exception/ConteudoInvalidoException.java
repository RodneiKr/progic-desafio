package br.com.progic.desafio.exception;

import java.util.Map;

public class ConteudoInvalidoException extends RuntimeException {

    private Map<String, String> detalhes;

    public ConteudoInvalidoException(final String message, final Map<String, String> detalhes) {
        super(message);
        this.detalhes = detalhes;
    }

    public Map<String, String> getDetalhes() {
        return this.detalhes;
    }
}
