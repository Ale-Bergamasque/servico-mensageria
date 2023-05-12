package br.com.servicomensageria.dto;

import br.com.servicomensageria.model.Usuario;
import lombok.Getter;

@Getter
public class DadosDetalhamentoUsuarioDto {

    private Long id;

    private String nome;

    private String email;

    private String cpf;

    public DadosDetalhamentoUsuarioDto(Usuario model) {
        this.id = model.getId();
        this.nome = model.getNome();
        this.email = model.getEmail();
        this.cpf = model.getCpf();
    }
}
