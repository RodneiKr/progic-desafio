package br.com.progic.desafio.service;

import br.com.progic.desafio.dto.UsuarioDto;
import br.com.progic.desafio.exception.ConteudoInvalidoException;
import br.com.progic.desafio.exception.NaoEncontradoException;
import br.com.progic.desafio.model.Usuario;
import br.com.progic.desafio.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    private static final Long ID = 1L;
    private static final String NOME = "nome";
    private static final String EMAIL = "nome@gmail.com";

    private UsuarioService service;

    private UsuarioRepository repository;

    @BeforeEach
    void beforeEach() {
        this.service = new UsuarioService();
        this.repository = mock(UsuarioRepository.class);
        ReflectionTestUtils.setField(this.service,"repository",this.repository);
    }

    @Test
    void incluirComSucesso() {
        final var dtoIncluir = this.service.incluir(this.usuarioDto());
        assertNull(dtoIncluir.getId());
        assertEquals(NOME,dtoIncluir.getNome());
        assertEquals(EMAIL,dtoIncluir.getEmail());
    }

    @Test
    void incluirComNomeEEmailNull() {
        try {
            final var dto = new UsuarioDto.Builder().build();
            final var dtoIncluir = this.service.incluir(dto);
            assertTrue(false);
        } catch (ConteudoInvalidoException e) {
            assertTrue(true);
        }
    }

    @Test
    void incluirComEmailDuplicado() {
        try {
            when(this.repository.findByEmail(anyString())).thenReturn(new Usuario());
            final var dtoIncluir = this.service.incluir(this.usuarioDto());
            assertTrue(false);
        } catch (ConteudoInvalidoException e) {
            assertTrue(true);
        }
    }

    @Test
    void alterarComSucesso() {
        when(this.repository.findById(anyLong())).thenReturn(Optional.of(new Usuario()));
        final var dtoAlterar = this.service.alterar(this.usuarioDto());
        assertEquals(NOME,dtoAlterar.getNome());
        assertEquals(EMAIL,dtoAlterar.getEmail());
    }

    @Test
    void alterarComUsuarioNaoEncontrado() {
        try {
            this.service.alterar(this.usuarioDto());
            assertTrue(false);
        } catch (NaoEncontradoException e) {
            assertTrue(true);
        }
    }

    @Test
    void excluirComSucesso() {
        when(this.repository.findById(anyLong())).thenReturn(Optional.of(new Usuario()));
        assertDoesNotThrow(() -> this.service.excluir(ID));
    }

    @Test
    void excluirComUsuarioNaoEncontrado() {
        try {
            this.service.excluir(ID);
            assertTrue(false);
        } catch (NaoEncontradoException e) {
            assertTrue(true);
        }
    }

    @Test
    void buscarPorIdComSucesso() {
        when(this.repository.findById(anyLong())).thenReturn(Optional.of(new Usuario()));
        final var dtoBuscar = this.service.buscarPorId(ID);
    }

    @Test
    void buscarPorIdComUsuarioNaoEncotrado() {
        try {
            final var dtoBuscar = this.service.buscarPorId(ID);
            assertTrue(false);
        } catch (NaoEncontradoException e) {
            assertTrue(true);
        }
    }

    @Test
    void buscarTodosComSucesso() {
        final var usuarios = Arrays.asList(new Usuario());
        final var page = new PageImpl<>(usuarios);
        when(this.repository.findAll(any(PageRequest.class))).thenReturn(page);
        this.service.buscarTodos(0,10,NOME);
    }

    private UsuarioDto usuarioDto() {
        return new UsuarioDto.Builder()
                .id(ID)
                .nome(NOME)
                .email(EMAIL)
                .build();
    }
}
