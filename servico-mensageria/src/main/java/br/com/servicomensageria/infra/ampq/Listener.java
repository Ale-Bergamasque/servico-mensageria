package br.com.servicomensageria.infra.ampq;

import br.com.servicomensageria.dto.DadosCadastroUsuarioDto;
import br.com.servicomensageria.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Listener {

    private final UsuarioService service;

    @RabbitListener(queues = "cadastro.usuario")
    public void recebeMensagem(DadosCadastroUsuarioDto dto) {

        String mensagem = service.cadastrarUsuarioMensageria(dto);
        System.out.println(mensagem);
    }
}
