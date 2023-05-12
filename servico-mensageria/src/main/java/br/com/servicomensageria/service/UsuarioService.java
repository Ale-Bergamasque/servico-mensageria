package br.com.servicomensageria.service;

import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {
    ResponseEntity cadastro(DadosCadastroUsuarioDto dadosCadastro);
}
