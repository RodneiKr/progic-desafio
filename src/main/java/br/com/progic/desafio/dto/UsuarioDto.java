package br.com.progic.desafio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record UsuarioDto(
        Long id,
        @NotBlank(message = "deve ser preenchido!")
        String nome,
        @Email(message = "invalido")
        @NotBlank(message = "deve ser preenchido!")
        String email,
        LocalDateTime dataCriacao
    ) {
}
