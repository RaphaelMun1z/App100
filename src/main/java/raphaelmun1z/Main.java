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
        long tempoInicialRK = System.nanoTime();
        chatServico.enviarMensagem(chat1, msg2);
        long tempoFinalRK = System.nanoTime();
        chatServico.imprimeMensagens(chat1);

        tipoCorridaServico.obterTodos().forEach(IO::println);

        long tempoInicialSeq = System.nanoTime();
        IO.println(tipoCorridaServico.obterViaIdBuscaSequencial(3L));
        long tempoFinalSeq = System.nanoTime();

        long tempoInicialBin = System.nanoTime();
        IO.println(passageiroServico.obterViaBuscaBinaria(3L));
        long tempoFinalBin = System.nanoTime();

        long tempoDecorridoBin = tempoFinalBin - tempoInicialBin;
        long tempoDecorridoSeq = tempoFinalSeq - tempoInicialSeq;
        long tempoDecorridoRK = tempoFinalRK - tempoInicialRK;
        System.out.println("Tempo de busca sequencial: " + tempoDecorridoSeq + " nanossegundos.");
        System.out.println("Tempo de busca binária: " + tempoDecorridoBin + " nanossegundos.");
        System.out.println("Tempo de Rabin Karp: " + tempoDecorridoRK + " nanossegundos.");
    }
}