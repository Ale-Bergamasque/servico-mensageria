package br.com.servicomensageria.dto;

import lombok.Getter;

@Getter
public class ErroDto {

    private final String campo;

    private final String mensagem;

    public ErroDto(String campo, String mensagem){
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
