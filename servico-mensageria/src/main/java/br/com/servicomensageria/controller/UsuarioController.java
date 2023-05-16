package br.com.servicomensageria.controller;


import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import br.com.servicomensageria.dto.DadosListagemUsuarioDto;
import br.com.servicomensageria.dto.EmailUsuarioDto;
import br.com.servicomensageria.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuario")
@RequiredArgsConstructor
@Tag(name = "Usuario", description = "Cadastro, deletar, alterar, buscar e listar.")
public class UsuarioController {

    private final UsuarioService service;

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar", description = "Deletar usuario por ID.")

    public ResponseEntity deletarUsuario(@PathVariable Long id) {
        return service.excluirUsuario(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar", description = "Atualizar email de usuario informado por ID.")
    @ApiResponse(responseCode = "200")
    public ResponseEntity atualizarEmailUsuario(@PathVariable Long id, @RequestBody EmailUsuarioDto dto) {
        return service.atualilzarEmailUsuario(id, dto);
    }

    @PostMapping
    @Operation(summary = "Cadastrar", description = "Cadastra novo usuario.")
    @ApiResponse(responseCode = "201", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DadosCadastroUsuarioDto.class))})
    public ResponseEntity cadastrarUsuario(@RequestBody DadosCadastroUsuarioDto dadosCadastro){
        return service.cadastroUsuario(dadosCadastro);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar", description = "Buscar um unico usuario por ID.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DadosListagemUsuarioDto.class))})
    public ResponseEntity buscarUsuario(@PathVariable Long id){
        return service.buscarUsuario(id);
    }

    @GetMapping
    @Operation(summary = "Listar", description = "Lista todos os usuarios paginados por padrao contendo 10 dados ordenados por ID.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DadosListagemUsuarioDto.class))})
    public ResponseEntity<Page<DadosListagemUsuarioDto>> listarUsuarios(@PageableDefault(size = 10, sort = {"id"})Pageable paginacao){
        return service.listarUsuarios(paginacao);
    }
}
