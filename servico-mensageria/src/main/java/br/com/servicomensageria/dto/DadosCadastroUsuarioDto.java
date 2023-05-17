package br.com.servicomensageria.dto;

import br.com.servicomensageria.model.Usuario;
import lombok.Data;

@Data
public class DadosCadastroUsuarioDto {

    private String nome;

    private String email;

    private String cpf;

    public static Usuario criaUsuario(DadosCadastroUsuarioDto dadosCadastro) {
        Usuario usuario = new Usuario();
        usuario.setNome(dadosCadastro.getNome());
        usuario.setEmail(dadosCadastro.getEmail());
        usuario.setCpf(dadosCadastro.getCpf());
        return usuario;
    }
}
