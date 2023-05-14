package br.com.servicomensageria.service;

import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import org.springframework.http.ResponseEntity;
import br.com.servicomensageria.dto.EmailUsuarioDto;

public interface UsuarioService {

    ResponseEntity excluirUsuário(Long id);
    ResponseEntity atualilzarEmailUsuario(Long id, EmailUsuarioDto dto);
    ResponseEntity cadastroUsuario(DadosCadastroUsuarioDto dadosCadastro);
    ResponseEntity buscarUsuario(Long id);
}
