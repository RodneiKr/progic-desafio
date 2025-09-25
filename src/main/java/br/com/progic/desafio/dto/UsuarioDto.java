package br.com.progic.desafio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UsuarioDto {

    private Long id;

    @NotNull(message = "deve ser preenchido")
    private String nome;

    @Email(message = "invalido")
    @NotNull(message = "deve ser preenchido")
    private String email;

    private LocalDateTime dataCriacao;

    public static class Builder {

        private final UsuarioDto dto = new UsuarioDto();

        public Builder id(final Long value) {
            this.dto.id = value;
            return this;
        }

        public Builder nome(final String value) {
            this.dto.nome = value;
            return this;
        }

        public Builder email(final String value) {
            this.dto.email = value;
            return this;
        }

        public Builder dataCriacao(final LocalDateTime value) {
            this.dto.dataCriacao = value;
            return this;
        }

        public UsuarioDto build() {
            return this.dto;
        }
    }
}
