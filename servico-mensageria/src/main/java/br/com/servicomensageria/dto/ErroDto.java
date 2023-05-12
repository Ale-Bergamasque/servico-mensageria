package br.com.servicomensageria.dto;

import lombok.Getter;

@Getter
public class ErroDto {

    private String campo;

    private String mensagem;

    public ErroDto(String campo, String mensagem){
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
