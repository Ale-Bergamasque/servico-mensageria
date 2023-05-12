package br.com.servicomensageria.controller;

import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import br.com.servicomensageria.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuario")
public class UsuarioController {

    private final UsuarioService service;


    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody DadosCadastroUsuarioDto dadosCadastro){
        return service.cadastro(dadosCadastro);
    }
}
