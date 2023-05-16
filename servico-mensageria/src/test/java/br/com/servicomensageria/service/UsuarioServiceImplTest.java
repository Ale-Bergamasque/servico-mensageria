package br.com.servicomensageria.service;

import br.com.servicomensageria.dto.EmailUsuarioDto;
import br.com.servicomensageria.model.Usuario;
import br.com.servicomensageria.repository.UsuarioRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.jeasy.random.EasyRandom;

import java.util.Optional;

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

    @Test
    public void deveriaExcluirUsuárioComSucesso() {
        doReturn(true).when(repository).existsById(ID);

        service.excluirUsuário(ID);

        verify(repository, atLeastOnce()).deleteById(ID);;
    }

    @Test
    public void naoDeveriaExcluirUsuárioComSucesso() {
        doReturn(false).when(repository).existsById(ID);

        service.excluirUsuário(ID);

        verify(repository, never()).deleteById(ID);;
    }

    @Test
    public void deveriaAtualizarUsuárioComSucesso() {
        String email = "123@email.com";
        EmailUsuarioDto novoEmail = new EmailUsuarioDto(email);
        Usuario usuario = new Usuario();
        Usuario usuarioOptional = new EasyRandom().nextObject(Usuario.class);

        doReturn(true).when(repository).existsById(ID);
        doReturn(false).when(repository).existsByEmail(email);
        doReturn(Optional.of(usuarioOptional)).when(repository).findById(ID);

        service.atualilzarEmailUsuario(ID, novoEmail);

        //TODO ajustar teste

        Assertions.assertEquals(email, usuario.getEmail());
        verify(repository, atLeastOnce()).save(usuario);
    }


}