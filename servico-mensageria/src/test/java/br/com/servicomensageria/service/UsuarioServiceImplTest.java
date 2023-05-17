package br.com.servicomensageria.service;

import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import br.com.servicomensageria.dto.EmailUsuarioDto;
import br.com.servicomensageria.infra.erros.ErroDto;
import br.com.servicomensageria.infra.erros.ValidarDados;
import br.com.servicomensageria.model.Usuario;
import br.com.servicomensageria.repository.UsuarioRepository;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceImplTest {

    private static final Long ID = 1L;

    @InjectMocks
    private UsuarioServiceImpl service;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private ValidarDados validarDados;

    @Test
    public void deveriaExcluirUsuarioComSucesso() {
        doReturn(true).when(repository).existsById(ID);

        service.excluirUsuario(ID);

        verify(repository, atLeastOnce()).deleteById(ID);
    }

    @Test
    public void naoDeveriaExcluirUsuarioInexistente() {
        doReturn(false).when(repository).existsById(ID);

        service.excluirUsuario(ID);

        verify(repository, never()).deleteById(ID);
    }

    @Test
    public void deveriaAtualizarUsuarioComSucesso() {
        String email = "123@email.com";
        EmailUsuarioDto novoEmail = new EmailUsuarioDto(email);
        Usuario usuarioOptional = new EasyRandom().nextObject(Usuario.class);

        doReturn(true).when(repository).existsById(ID);
        doReturn(false).when(repository).existsByEmail(email);
        doReturn(Optional.of(usuarioOptional)).when(repository).findById(ID);

        service.atualilzarEmailUsuario(ID, novoEmail);

        Assertions.assertEquals(email, usuarioOptional.getEmail());
        verify(repository, atLeastOnce()).save(any());
    }

    @Test
    public void naoDeveriaAtualizarUsuarioComEmailExistente() {
        String email = "123@email.com";
        EmailUsuarioDto novoEmail = new EmailUsuarioDto(email);

        doReturn(true).when(repository).existsById(ID);
        doReturn(true).when(repository).existsByEmail(email);

        service.atualilzarEmailUsuario(ID, novoEmail);

        verify(repository, never()).save(any());
    }

    @Test
    public void naoDeveriaAtualizarUsuarioInexistente() {
        String email = "123@email.com";
        EmailUsuarioDto novoEmail = new EmailUsuarioDto(email);

        doReturn(false).when(repository).existsById(ID);

        service.atualilzarEmailUsuario(ID, novoEmail);

        verify(repository, never()).save(any());
    }

    @Test
    public void naoDeveriaCadastrarUsuarioPorMensageriaComSucesso() {
        DadosCadastroUsuarioDto dados = new EasyRandom().nextObject(DadosCadastroUsuarioDto.class);

        doReturn(false).when(repository).existsByCpf(dados.getCpf());
        doReturn(false).when(repository).existsByEmail(dados.getEmail());
        doReturn(EMPTY_LIST).when(validarDados).cadastro(dados);

        service.cadastrarUsuarioMensageria(dados);

        verify(repository, atLeastOnce()).save(any());
    }

    @Test
    public void deveriaCadastrarUsuarioPorMensageriaComEmailExistente() {
        DadosCadastroUsuarioDto dados = new EasyRandom().nextObject(DadosCadastroUsuarioDto.class);

        doReturn(false).when(repository).existsByCpf(dados.getCpf());
        doReturn(true).when(repository).existsByEmail(dados.getEmail());

        service.cadastrarUsuarioMensageria(dados);

        verify(repository, never()).save(any());
    }

    @Test
    public void deveriaCadastrarUsuarioPorMensageriaComCpfExistente() {
        DadosCadastroUsuarioDto dados = new EasyRandom().nextObject(DadosCadastroUsuarioDto.class);

        doReturn(true).when(repository).existsByCpf(dados.getCpf());

        service.cadastrarUsuarioMensageria(dados);

        verify(repository, never()).save(any());
    }

    @Test
    public void deveriaCadastrarUsuarioPorMensageriaComCamposVazios() {
        DadosCadastroUsuarioDto dados = new EasyRandom().nextObject(DadosCadastroUsuarioDto.class);

        doReturn(false).when(repository).existsByCpf(dados.getCpf());
        doReturn(false).when(repository).existsByEmail(dados.getEmail());
        doReturn(singletonList(new ErroDto("Nome", "Erro"))).when(validarDados).cadastro(dados);

        service.cadastrarUsuarioMensageria(dados);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deveria cadastrar o usuario com sucesso")
    public void cadastrarUsuarioComSucesso() {
        DadosCadastroUsuarioDto dadosCadastro = new EasyRandom().nextObject(DadosCadastroUsuarioDto.class);

        List<ErroDto> erros = new ArrayList<>();

        when(repository.existsByCpf(dadosCadastro.getCpf())).thenReturn(false);
        when(validarDados.cadastro(dadosCadastro)).thenReturn(erros);

        service.cadastroUsuario(dadosCadastro);

        verify(repository, atLeastOnce()).save(any());
    }

    @Test
    @DisplayName("Nao deveria cadastrar o usuario por falha no preenchimento dos campos de cadastro")
    public void naoDeveriaCadastrar(){
        DadosCadastroUsuarioDto dadosCadastroUsuario = new EasyRandom().nextObject(DadosCadastroUsuarioDto.class);

        List<ErroDto> erros = new ArrayList<>();
        erros.add(0, new ErroDto("campo", "mensagem"));

        when(repository.existsByCpf(dadosCadastroUsuario.getCpf())).thenReturn(false);
        when(validarDados.cadastro(dadosCadastroUsuario)).thenReturn(erros);

        service.cadastroUsuario(dadosCadastroUsuario);

        verify(validarDados, atLeastOnce()).cadastro(dadosCadastroUsuario);
    }

    @Test
    @DisplayName("Nao deveria cadastrar o usuario por existir usuario com o mesmo CPF no banco")
    public void naoDeveriaCadastrarCpfExistente(){
        DadosCadastroUsuarioDto dadosCadastroUsuario = new EasyRandom().nextObject(DadosCadastroUsuarioDto.class);

        when(repository.existsByCpf(dadosCadastroUsuario.getCpf())).thenReturn(true);

        ResponseEntity response_method = service.cadastroUsuario(dadosCadastroUsuario);

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("teste");

        assertThat(response_method.getStatusCode()).isEqualTo(response.getStatusCode());
        verify(repository, atLeastOnce()).existsByCpf(dadosCadastroUsuario.getCpf());
    }

    @Test
    @DisplayName("Nao deveria cadastrar o usuario por existir usuario com o mesmo Email no banco")
    public void naoDeveriaCadastrarEmailExistente(){
        DadosCadastroUsuarioDto dadosCadastroUsuario = new EasyRandom().nextObject(DadosCadastroUsuarioDto.class);

        when(repository.existsByCpf(dadosCadastroUsuario.getCpf())).thenReturn(false);
        when(repository.existsByEmail(dadosCadastroUsuario.getEmail())).thenReturn(true);

        ResponseEntity resposta = service.cadastroUsuario(dadosCadastroUsuario);

        ResponseEntity<String> codigoEsperado = ResponseEntity.status(HttpStatus.CONFLICT).body("teste");

        assertThat(resposta.getStatusCode()).isEqualTo(codigoEsperado.getStatusCode());
        verify(repository, atLeastOnce()).existsByEmail(dadosCadastroUsuario.getEmail());
    }

    @Test
    @DisplayName("Deveria retornar os dados do usuario")
    public void deveriaRetornarDadosUsuario(){
        Usuario usuario = new EasyRandom().nextObject(Usuario.class);
        when(repository.getReferenceById(ID)).thenReturn(usuario);
        when(repository.existsById(ID)).thenReturn(true);

        service.buscarUsuario(ID);

        verify(repository, atLeastOnce()).getReferenceById(ID);
    }

    @Test
    @DisplayName("Nao deveria retornar os dados do usuario por nao existir no banco")
    public void naoDeveriaRetornarDadosUsuario(){
        when(repository.existsById(ID)).thenReturn(false);

        ResponseEntity resposta = service.buscarUsuario(ID);

        ResponseEntity<String> codigoEsperado = ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        verify(repository, atLeastOnce()).existsById(ID);
        assertThat(resposta.getStatusCode()).isEqualTo(codigoEsperado.getStatusCode());
    }

}