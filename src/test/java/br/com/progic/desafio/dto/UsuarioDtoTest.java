package br.com.progic.desafio.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDtoTest {

    private static final List<String> MENSAGENS = List.of("nome deve ser preenchido!","email deve ser preenchido!");
    private Validator validator;

    @BeforeEach
    void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validarPropriedades() {
        final var dto = new UsuarioDto(
                0L,
                " ",
                "",
                null
        );
        final Set<ConstraintViolation<UsuarioDto>> violations = this.validator.validate(dto);
//        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        violations.forEach(e -> assertTrue(MENSAGENS.contains(e.getPropertyPath() + " " + e.getMessage())));
    }
}
