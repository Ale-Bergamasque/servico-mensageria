package br.com.servicomensageria.service;

import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import br.com.servicomensageria.dto.DadosDetalhamentoUsuarioDto;
import br.com.servicomensageria.dto.EmailUsuarioDto;
import br.com.servicomensageria.dto.ErroDto;
import br.com.servicomensageria.model.Usuario;
import br.com.servicomensageria.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository repository;

    @Override
    @Transactional
    public ResponseEntity cadastro(DadosCadastroUsuarioDto dadosCadastro) {
        if (repository.existsByCpf(dadosCadastro.getCpf())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario com o cpf " + dadosCadastro.getCpf() + " já cadastrado!");
        }

        List<ErroDto> erros = validarDadosCadastro(dadosCadastro);
        if (!erros.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Um ou mais campos informados incorretamente!");
        }

        var model = new Usuario(dadosCadastro);

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

    private List<ErroDto> validarDadosCadastro(DadosCadastroUsuarioDto dadosCadastro) {
        List<ErroDto> erros = new ArrayList<>();
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
