package raphaelmun1z;

import raphaelmun1z.entidades.Chat;
import raphaelmun1z.entidades.Mensagem;
import raphaelmun1z.entidades.Motorista;
import raphaelmun1z.entidades.Passageiro;
import raphaelmun1z.servicos.ChatServico;
import raphaelmun1z.servicos.MotoristaServico;
import raphaelmun1z.servicos.PassageiroServico;
import raphaelmun1z.servicos.TipoCorridaServico;

public class Main {
    public static void main() {
        MotoristaServico motoristaServico = new MotoristaServico();
        motoristaServico.mockDados();

        PassageiroServico passageiroServico = new PassageiroServico();
        passageiroServico.mockDados();

        TipoCorridaServico tipoCorridaServico = new TipoCorridaServico();
        tipoCorridaServico.mockDados();

        ChatServico chatServico = new ChatServico();
        chatServico.mockDados();

        Motorista m1 = motoristaServico.obterTodos().getFirst();
        Passageiro p1 = passageiroServico.obterTodos().getFirst();
        Chat chat1 = new Chat(1L, m1, p1);
        Mensagem msg1 = new Mensagem(1L, p1, m1, "Bom dia!");
        Mensagem msg2 = new Mensagem(1L, p1, m1, "Teste, golpe, 123");
        chatServico.enviarMensagem(chat1, msg1);
        chatServico.enviarMensagem(chat1, msg2);
        chatServico.imprimeMensagens(chat1);

        tipoCorridaServico.obterTodos().forEach(IO::println);

        IO.println(tipoCorridaServico.obterViaIdBuscaSequencial(2L));
        IO.println(passageiroServico.obterViaBuscaBinaria(3L));
    }
}