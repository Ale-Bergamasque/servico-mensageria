package br.com.servicomensageria.dto;

import br.com.servicomensageria.model.Usuario;
import lombok.Getter;

@Getter
public class DadosDetalhamentoUsuarioDto {

    private final Long id;

    private final String nome;

    private final String email;

    private final String cpf;

    public DadosDetalhamentoUsuarioDto(Usuario model) {
        this.id = model.getId();
        this.nome = model.getNome();
        this.email = model.getEmail();
        this.cpf = model.getCpf();
    }
}
