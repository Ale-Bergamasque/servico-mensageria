package br.com.servicomensageria.model;

import jakarta.persistence.*;
import lombok.Data;

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

}
