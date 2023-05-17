package br.com.servicomensageria.controller;

import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import br.com.servicomensageria.dto.DadosDetalhamentoUsuarioDto;
import br.com.servicomensageria.dto.DadosListagemUsuarioDto;
import br.com.servicomensageria.model.Usuario;
import br.com.servicomensageria.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UsuarioService service;

    @Autowired
    private JacksonTester<DadosCadastroUsuarioDto> dadosCadastroUsuarioJSON;

    @Autowired
    private JacksonTester<DadosDetalhamentoUsuarioDto> dadosDetalhamentoUsuarioJSON;

    @Autowired
    private JacksonTester<DadosListagemUsuarioDto> dadosListagemUsuarioJSON;

    @Test
    @DisplayName("Deveria devolver codigo HTTP 201 indicando que o usuario foi cadastrado com sucesso")
    public void cadastrar_Created() throws Exception {
        DadosCadastroUsuarioDto dadosCadastroUsuario = new DadosCadastroUsuarioDto();
        dadosCadastroUsuario.setNome("teste");
        dadosCadastroUsuario.setEmail("@teste.com");
        dadosCadastroUsuario.setCpf("123456");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome(dadosCadastroUsuario.getNome());
        usuario.setEmail(dadosCadastroUsuario.getEmail());
        usuario.setCpf(dadosCadastroUsuario.getCpf());

        DadosDetalhamentoUsuarioDto dadosDetalhamentoUsuario = new DadosDetalhamentoUsuarioDto(usuario);

        when(service.cadastroUsuario(dadosCadastroUsuario))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(dadosDetalhamentoUsuario));

        MockHttpServletResponse response = mvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroUsuarioJSON.write(
                        dadosCadastroUsuario
                ).getJson()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        String jsonEsperado = dadosDetalhamentoUsuarioJSON
                .write(dadosDetalhamentoUsuario)
                .getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria retornar codigo 400 quando informações estão inválidas")
    public void cadastrar_BadRequest() throws Exception {
        MockHttpServletResponse response =
                mvc.perform(post("/usuario"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria retornar codigo HTTP 200")
    void buscarUsuario_Ok() throws Exception {
        DadosCadastroUsuarioDto dadosCadastroUsuario = new DadosCadastroUsuarioDto();
        dadosCadastroUsuario.setNome("teste");
        dadosCadastroUsuario.setEmail("@teste.com");
        dadosCadastroUsuario.setCpf("123456");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome(dadosCadastroUsuario.getNome());
        usuario.setEmail(dadosCadastroUsuario.getEmail());
        usuario.setCpf(dadosCadastroUsuario.getCpf());

        DadosListagemUsuarioDto dadosListagemUsuario = new DadosListagemUsuarioDto(usuario);

        when(service.buscarUsuario(1L))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(dadosListagemUsuario));

        MockHttpServletResponse response =
                mvc.perform(get("/usuario/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dadosListagemUsuarioJSON.write(
                                        dadosListagemUsuario
                        ).getJson()))
                        .andReturn()
                        .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosListagemUsuarioJSON
                .write(dadosListagemUsuario)
                .getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

}