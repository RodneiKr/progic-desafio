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
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public UsuarioDto incluir(final UsuarioDto dto) {
        this.validar(dto);
        final var entity = this.dePara(dto);
        this.repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public UsuarioDto alterar(final UsuarioDto dto) {
        this.validar(dto);
        final var usuario = this.buscarUsuarioPorId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        this.repository.save(usuario);
        return dto;
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

    private UsuarioDto buscarUsuarioPorEmail(final String email) {
        return this.dePara(this.repository.findByEmail(email));
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
        if (de == null) {
            return null;
        }
        return new UsuarioDto.Builder()
                .id(de.getId())
                .nome(de.getNome())
                .email(de.getEmail())
                .dataCriacao(de.getDataCriacao())
                .build();
    }

    private void validar(final UsuarioDto dto) {
        final var erros = new HashMap<String, String>();
        if (dto.getEmail() != null && this.buscarUsuarioPorEmail(dto.getEmail()) != null) {
                erros.put("email", "'" + dto.getEmail() + "' ja esta cadastrado. ");
        }
        if (! erros.isEmpty()) {
            throw new ConteudoInvalidoException("dado(s) invalido(s)", erros);
        }
    }
}
