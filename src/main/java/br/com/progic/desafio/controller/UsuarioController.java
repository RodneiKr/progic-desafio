package br.com.progic.desafio.controller;

import br.com.progic.desafio.dto.UsuarioDto;
import br.com.progic.desafio.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public @ResponseBody UsuarioDto post(
            @Valid @RequestBody final UsuarioDto dto
    ) {
        return this.service.incluir(dto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping()
    public @ResponseBody UsuarioDto put(
            @Valid @RequestBody final UsuarioDto dto
    ) {
        return this.service.alterar(dto);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public @ResponseBody String delete(
            @PathVariable final Long id
    ) {
        this.service.excluir(id);
        return "{\"mensagem\":\"id " + id + " excluido com sucesso\"}";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public @ResponseBody UsuarioDto getById(
            @PathVariable final Long id
    ) {
        return this.service.buscarPorId(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/todos")
    public @ResponseBody List<UsuarioDto> getByAll(
            @QueryParam("pagina") final Integer pagina,
            @QueryParam("qtd") final Integer qtdUsuarios
    ) {
        return this.service.buscarTodos(pagina,qtdUsuarios, "nome");
    }
}
