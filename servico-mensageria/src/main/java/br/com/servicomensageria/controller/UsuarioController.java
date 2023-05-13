package br.com.servicomensageria.controller;


import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import br.com.servicomensageria.dto.EmailUsuarioDto;
import br.com.servicomensageria.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity cadastrar(@RequestBody DadosCadastroUsuarioDto dadosCadastro){
        return service.cadastro(dadosCadastro);
    }
}
