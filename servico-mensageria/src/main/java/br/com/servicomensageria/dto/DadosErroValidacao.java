package br.com.servicomensageria.dto;

import br.com.servicomensageria.infra.erros.ErroDto;
import lombok.Getter;

@Getter
public class DadosErroValidacao {

    private final String campo;

    private final String mensagem;

    public DadosErroValidacao(ErroDto erros){
        this.campo = erros.getCampo();
        this.mensagem = erros.getMensagem();
    }
}
