package br.com.servicomensageria.service;

import br.com.servicomensageria.dto.*;
import br.com.servicomensageria.model.Usuario;
import br.com.servicomensageria.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository repository;

    @Override
    @Transactional
    public ResponseEntity cadastroUsuario(DadosCadastroUsuarioDto dadosCadastro) {
        List<ErroDto> erros = validarDadosCadastro(dadosCadastro);
        if (!erros.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros.stream().map(DadosErroValidacao::new));
        }

        Usuario model = new Usuario(dadosCadastro);

        repository.save(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalhamentoUsuarioDto(model));
    }

    @Override
    @Transactional
    public ResponseEntity excluirUsuário(Long id) {
        if(!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usuário excluído com sucesso!");
    }

    @Override
    @Transactional
    public ResponseEntity atualilzarEmailUsuario(Long id, EmailUsuarioDto dto) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        if(repository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario com o email " + dto.getEmail() + " já cadastrado!");
        }

        Usuario usuario = repository.findById(id).get();

        usuario.setEmail(dto.getEmail());

        repository.save(usuario);
        return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso!");
    }

    @Override
    public ResponseEntity buscarUsuario(Long id) {
        String mensagem = "ERROR: Id nao encontrado!";
        if (!repository.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosErroValidacao(new ErroDto("id", "Não foram encontrados dados para o ID informado.")));
        }
        Usuario model = repository.getReferenceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DadosListagemUsuarioDto(model));
    }

    @Override
    public ResponseEntity<Page<DadosListagemUsuarioDto>> listarUsuarios(Pageable paginacao) {
        Page<DadosListagemUsuarioDto> model = repository.findAll(paginacao).map(DadosListagemUsuarioDto::new);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    private List<ErroDto> validarDadosCadastro(DadosCadastroUsuarioDto dadosCadastro) {
        List<ErroDto> erros = new ArrayList<>();

        if (repository.existsByCpf(dadosCadastro.getCpf())){
            erros.add(new ErroDto("cpf", "CPF já cadastrado"));
        }
        if (dadosCadastro.getNome() == null || dadosCadastro.getNome().isEmpty()){
            erros.add(new ErroDto("nome", "Campo obrigatório"));
        }
        if (dadosCadastro.getEmail() == null || dadosCadastro.getEmail().isEmpty()){
            erros.add(new ErroDto("email", "Campo obrigatório"));
        }
        if (dadosCadastro.getCpf() == null || dadosCadastro.getCpf().isEmpty()){
            erros.add(new ErroDto("cpf", "Campo obrigatório"));
        }
        return erros;
    }
}
