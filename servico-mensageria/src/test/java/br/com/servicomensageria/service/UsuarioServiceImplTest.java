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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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

        verify(repository, atLeastOnce()).deleteById(ID);;
    }

    @Test
    public void naoDeveriaExcluirUsuarioInexistente() {
        doReturn(false).when(repository).existsById(ID);

        service.excluirUsuario(ID);

        verify(repository, never()).deleteById(ID);;
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

        verify(repository, atLeastOnce()).save(any());;
    }

    @Test
    public void deveriaCadastrarUsuarioPorMensageriaComEmailExistente() {
        DadosCadastroUsuarioDto dados = new EasyRandom().nextObject(DadosCadastroUsuarioDto.class);

        doReturn(false).when(repository).existsByCpf(dados.getCpf());
        doReturn(true).when(repository).existsByEmail(dados.getEmail());

        service.cadastrarUsuarioMensageria(dados);

        verify(repository, never()).save(any());;
    }

    @Test
    public void deveriaCadastrarUsuarioPorMensageriaComCpfExistente() {
        DadosCadastroUsuarioDto dados = new EasyRandom().nextObject(DadosCadastroUsuarioDto.class);

        doReturn(true).when(repository).existsByCpf(dados.getCpf());

        service.cadastrarUsuarioMensageria(dados);

        verify(repository, never()).save(any());;
    }

    @Test
    public void deveriaCadastrarUsuarioPorMensageriaComCamposVazios() {
        DadosCadastroUsuarioDto dados = new EasyRandom().nextObject(DadosCadastroUsuarioDto.class);

        doReturn(false).when(repository).existsByCpf(dados.getCpf());
        doReturn(false).when(repository).existsByEmail(dados.getEmail());
        doReturn(singletonList(new ErroDto("Nome", "Erro"))).when(validarDados).cadastro(dados);

        service.cadastrarUsuarioMensageria(dados);

        verify(repository, never()).save(any());;
    }
}