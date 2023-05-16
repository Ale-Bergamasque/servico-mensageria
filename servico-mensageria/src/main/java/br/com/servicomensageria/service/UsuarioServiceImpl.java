package br.com.servicomensageria.service;

import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import br.com.servicomensageria.dto.DadosDetalhamentoUsuarioDto;
import br.com.servicomensageria.dto.DadosErroValidacao;
import br.com.servicomensageria.dto.DadosListagemUsuarioDto;
import br.com.servicomensageria.dto.EmailUsuarioDto;
import br.com.servicomensageria.infra.erros.ErroDto;
import br.com.servicomensageria.infra.erros.ValidarDados;
import br.com.servicomensageria.model.Usuario;
import br.com.servicomensageria.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository repository;

    private final ValidarDados validarDados;

    @Override
    @Transactional
    public ResponseEntity cadastroUsuario(DadosCadastroUsuarioDto dadosCadastro) {
        if (repository.existsByCpf(dadosCadastro.getCpf())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DadosErroValidacao(new ErroDto("cpf", "CPF já cadastrado.")));
        }
        List<ErroDto> erros = validarDados.cadastro(dadosCadastro);
        if (!erros.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros.stream().map(DadosErroValidacao::new));
        }

        Usuario model = new Usuario(dadosCadastro);

        repository.save(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalhamentoUsuarioDto(model));
    }

    @Override
    @Transactional
    public ResponseEntity excluirUsuario(Long id) {
        if(!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usuário excluído com sucesso!");
    }

    @Override
    @Transactional
    public ResponseEntity atualilzarEmailUsuario(Long id, EmailUsuarioDto dto) {
        Usuario usuario = new Usuario();
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        if(repository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario com o email " + dto.getEmail() + " já cadastrado!");
        }

        Optional<Usuario> usuarioOptional = repository.findById(id);

        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();
            usuario.setEmail(dto.getEmail());
        }

        repository.save(usuario);
        return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso!");
    }

    @Override
    public ResponseEntity buscarUsuario(Long id) {
        if (!repository.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosErroValidacao(new ErroDto("id", "Usuário nao encontrado para o ID informado.")));
        }
        Usuario model = repository.getReferenceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DadosListagemUsuarioDto(model));
    }

    @Override
    public ResponseEntity<Page<DadosListagemUsuarioDto>> listarUsuarios(Pageable paginacao) {
        Page<DadosListagemUsuarioDto> model = repository.findAll(paginacao).map(DadosListagemUsuarioDto::new);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    @Override
    @Transactional
    public String cadastrarUsuarioMensageria(DadosCadastroUsuarioDto dadosCadastro) {
        if (repository.existsByCpf(dadosCadastro.getCpf())){
            return "CPF já cadastrado.";
        }
        if (repository.existsByEmail(dadosCadastro.getEmail())){
            return "Email já cadastrado";
        }
        List<ErroDto> erros = validarDados.cadastro(dadosCadastro);
        if (!erros.isEmpty()){
            return "Todos os campos devem ser preenchidos.";
        }

        Usuario model = new Usuario(dadosCadastro);

        repository.save(model);
        return "Usuário cadastrado com sucesso! -> Id: " + model.getId() + " / Nome: " + model.getNome() + " / E-mail: "
                + model.getEmail() + " / CPF: " + model.getCpf();
    }

}
