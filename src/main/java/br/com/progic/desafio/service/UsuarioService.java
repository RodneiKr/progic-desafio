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
        return new UsuarioDto(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getDataCriacao()
        );
    }

    public UsuarioDto alterar(final UsuarioDto dto) {
        this.validar(dto);
        final var usuario = this.buscarUsuarioPorId(dto.id());
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        this.repository.save(usuario);
        return new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataCriacao()
        );
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
                new UsuarioDto(
                        e.getId(),
                        e.getNome(),
                        e.getEmail(),
                        e.getDataCriacao()
                )
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
        para.setNome(de.nome());
        para.setEmail(de.email());
        para.setDataCriacao(LocalDateTime.now());
        return para;
    }

    private UsuarioDto dePara(final Usuario de) {
        return new UsuarioDto(
                de.getId(),
                de.getNome(),
                de.getEmail(),
                de.getDataCriacao()
        );
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
        if (dto.nome() == null || dto.nome().isBlank()) {
            erros.put(NOME, "deve ser preenchido.");
        }
    }

    private void validarEmail(final UsuarioDto dto, final Map<String, String> erros) {
        if (dto.email() == null || dto.email().isBlank()) {
            erros.put(EMAIL, "deve ser preenchido.");
        }
        final var usuario = this.buscarUsuarioPorEmail(dto.email());
        final var jaExiste = (
            usuario != null && (
                (dto.id() == null)
                ||
                (! dto.id().equals(usuario.getId()))
            )
        );
        if (jaExiste) {
            erros.put(EMAIL, "'" + dto.email() + "' ja esta cadastrado.");
        }
    }
}
