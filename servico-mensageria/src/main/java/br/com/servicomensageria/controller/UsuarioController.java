package br.com.servicomensageria.controller;


import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import br.com.servicomensageria.dto.DadosListagemUsuarioDto;
import br.com.servicomensageria.dto.EmailUsuarioDto;
import br.com.servicomensageria.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @DeleteMapping("/{id}")
    public ResponseEntity deletarUsuario(@PathVariable Long id) {
        return service.excluirUsuário(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity atualizarEmailUsuario(@PathVariable Long id, @RequestBody EmailUsuarioDto dto) {
        return service.atualilzarEmailUsuario(id, dto);
    }

    @PostMapping
    public ResponseEntity cadastrarUsuario(@RequestBody DadosCadastroUsuarioDto dadosCadastro){
        return service.cadastroUsuario(dadosCadastro);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarUsuario(@PathVariable Long id){
        return service.buscarUsuario(id);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuarioDto>> listarUsuarios(@PageableDefault(size = 10, sort = {"id"})Pageable paginacao){
        return service.listarUsuarios(paginacao);
    }
}
