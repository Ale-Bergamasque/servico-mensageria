package br.com.servicomensageria.service;

import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import br.com.servicomensageria.dto.DadosListagemUsuarioDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import br.com.servicomensageria.dto.EmailUsuarioDto;

public interface UsuarioService {

    ResponseEntity excluirUsuario(Long id);
    ResponseEntity atualilzarEmailUsuario(Long id, EmailUsuarioDto dto);

    ResponseEntity cadastroUsuario(DadosCadastroUsuarioDto dadosCadastro);

    ResponseEntity buscarUsuario(Long id);

    ResponseEntity<Page<DadosListagemUsuarioDto>> listarUsuarios(Pageable paginacao);

    String cadastrarUsuarioMensageria(DadosCadastroUsuarioDto dadosCadastro);
}
