package br.com.progic.desafio.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ErroResponse {

    private Integer httpStatus;
    private String mensagem;
    private Map<String, String> detalhes;
    private LocalDateTime dataHora = LocalDateTime.now();

    private ErroResponse() {
    }

    public static class Builder {
        private final ErroResponse erro = new ErroResponse();

        public Builder httpStatus(final Integer value) {
            this.erro.httpStatus = value;
            return this;
        }

        public Builder mensagem(final String value) {
            this.erro.mensagem = value;
            return this;
        }

        public Builder detalhes(final Map<String, String> value) {
            this.erro.detalhes = value;
            return this;
        }

        public ErroResponse build() {
            return this.erro;
        }
    }
}