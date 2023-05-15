package br.com.servicomensageria.infra.erros;

import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;

import java.util.ArrayList;
import java.util.List;

public class ValidarDados {

    public List<ErroDto> cadastro(DadosCadastroUsuarioDto dadosCadastro) {
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
