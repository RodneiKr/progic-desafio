package br.com.progic.desafio.service;

import br.com.progic.desafio.dto.UsuarioDto;
import br.com.progic.desafio.exception.ConteudoInvalidoException;
import br.com.progic.desafio.exception.NaoEncontradoException;
import br.com.progic.desafio.model.Usuario;
import br.com.progic.desafio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    private static final String NOME = "nome";
    private static final String EMAIL = "email";

    @Autowired
    private UsuarioRepository repository;

    public UsuarioDto incluir(final UsuarioDto dto) {
        this.validar(dto);
        final var entity = this.dePara(dto);
        this.repository.save(entity);
        return new UsuarioDto.Builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .dataCriacao(entity.getDataCriacao())
                .build();
    }

    public UsuarioDto alterar(final UsuarioDto dto) {
        this.validar(dto);
        final var usuario = this.buscarUsuarioPorId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        this.repository.save(usuario);
        return new UsuarioDto.Builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .dataCriacao(usuario.getDataCriacao())
                .build();
    }

    public void excluir(final Long id) {
        this.buscarUsuarioPorId(id);
        this.repository.deleteById(id);
    }

    public UsuarioDto buscarPorId(final Long id) {
        return this.dePara(this.buscarUsuarioPorId(id));
    }

    public List<UsuarioDto> buscarTodos(final Integer pagina, final Integer qtdUsuarios, final String campo) {
        final var sort = Sort.by(Sort.Direction.ASC,campo);
        final var pageRequest = PageRequest.of(pagina,qtdUsuarios,sort);
        final var page = this.repository.findAll(pageRequest);
        final var ret = new ArrayList<UsuarioDto>();
        page.forEach(e -> ret.add(
                new UsuarioDto.Builder()
                        .id(e.getId())
                        .nome(e.getNome())
                        .email(e.getEmail())
                        .dataCriacao(e.getDataCriacao())
                        .build()
        ));
        return ret;
    }

    private Usuario buscarUsuarioPorEmail(final String email) {
        return this.repository.findByEmail(email);
    }

    private Usuario buscarUsuarioPorId(final Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Usuario nao encontrado: id " + id));
    }

    private Usuario dePara(final UsuarioDto de) {
        final var para = new Usuario();
        para.setId(null);
        para.setNome(de.getNome());
        para.setEmail(de.getEmail());
        para.setDataCriacao(LocalDateTime.now());
        return para;
    }

    private UsuarioDto dePara(final Usuario de) {
        return new UsuarioDto.Builder()
                .id(de.getId())
                .nome(de.getNome())
                .email(de.getEmail())
                .dataCriacao(de.getDataCriacao())
                .build();
    }

    private void validar(final UsuarioDto dto) {
        final var erros = new HashMap<String, String>();

        this.validarNome(dto, erros);
        this.validarEmail(dto, erros);

        if (! erros.isEmpty()) {
            throw new ConteudoInvalidoException("dado(s) invalido(s)", erros);
        }
    }

    private void validarNome(final UsuarioDto dto, final Map<String,String> erros) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            erros.put(NOME, "deve ser preenchido.");
        }
    }

    private void validarEmail(final UsuarioDto dto, final Map<String, String> erros) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            erros.put(EMAIL, "deve ser preenchido.");
        }
        final var usuario = this.buscarUsuarioPorEmail(dto.getEmail());
        final var jaExiste = (
            usuario != null && (
                (dto.getId() == null)
                ||
                (! dto.getId().equals(usuario.getId()))
            )
        );
        if (jaExiste) {
            erros.put(EMAIL, "'" + dto.getEmail() + "' ja esta cadastrado.");
        }
    }
}
