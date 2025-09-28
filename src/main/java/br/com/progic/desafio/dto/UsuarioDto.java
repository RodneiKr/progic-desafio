package br.com.progic.desafio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

public record UsuarioDto(
        Long id,
        @NotNull(message = "deve ser preenchido")
        String nome,
        @Email(message = "invalido")
        @NotNull(message = "deve ser preenchido")
        String email,
        LocalDateTime dataCriacao
    ) {
}
