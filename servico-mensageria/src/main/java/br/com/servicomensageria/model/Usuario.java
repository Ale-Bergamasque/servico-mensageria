package br.com.servicomensageria.model;

import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String cpf;

    public Usuario(DadosCadastroUsuarioDto dadosCadastro) {
        this.nome = dadosCadastro.getNome();
        this.email = dadosCadastro.getEmail();
        this.cpf = dadosCadastro.getCpf();
    }
}
