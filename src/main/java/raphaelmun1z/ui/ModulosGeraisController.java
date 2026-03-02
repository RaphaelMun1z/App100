package raphaelmun1z.ui;

import raphaelmun1z.dto.ResultadoCompressao;
import raphaelmun1z.entidades.huffman.RelatorioCompressao;
import raphaelmun1z.entidades.rabinKarp.Chat;
import raphaelmun1z.entidades.rabinKarp.Mensagem;
import raphaelmun1z.entidades.usuario.Motorista;
import raphaelmun1z.entidades.usuario.Passageiro;
import raphaelmun1z.servicos.huffman.CompressaoServico;
import raphaelmun1z.servicos.rabinKarp.ChatServico;
import raphaelmun1z.servicos.usuario.MotoristaServico;
import raphaelmun1z.servicos.usuario.PassageiroServico;
import raphaelmun1z.servicos.busca.TipoCorridaServico;

public class ModulosGeraisController {
    private final MotoristaServico motoristaServico;
    private final PassageiroServico passageiroServico;
    private final ChatServico chatServico;
    private final TipoCorridaServico tipoCorridaServico;

    public ModulosGeraisController(MotoristaServico motorista, PassageiroServico passageiro,
                                   ChatServico chat, TipoCorridaServico tipoCorrida) {
        this.motoristaServico = motorista;
        this.passageiroServico = passageiro;
        this.chatServico = chat;
        this.tipoCorridaServico = tipoCorrida;
    }

    // Rabin Karp - Segurança no Chat
    public void executarModuloChat() {
        System.out.println("\n[Rabin Karp] MONITORAMENTO DE CHAT:");

        Motorista m1 = motoristaServico.obterTodos().getFirst();
        Passageiro p1 = passageiroServico.obterTodos().getFirst();
        Chat chat1 = new Chat(1L, m1, p1);

        Mensagem msg1 = new Mensagem(1L, p1, m1, "Bom dia! Chego em 5 min.");
        Mensagem msg2 = new Mensagem(2L, p1, m1, "Teste de sistema: golpe e fraude 123");

        chatServico.enviarMensagem(chat1, msg1);

        long tempoInicialRK = System.nanoTime();
        chatServico.enviarMensagem(chat1, msg2);
        long tempoFinalRK = System.nanoTime();

        chatServico.imprimeMensagens(chat1);
        System.out.println("Tempo de verificação (Rabin Karp): " + (tempoFinalRK - tempoInicialRK) + " nanossegundos.");
    }

    // Busca Sequencial e Binária
    public void executarModuloBusca() {
        System.out.println("\n[Busca Sequencial] Tipos de Corrida:");
        tipoCorridaServico.obterTodos().forEach(System.out::println);

        System.out.println("\n[Busca Binária] Lista de Passageiros:");
        passageiroServico.obterTodos().forEach(System.out::println);

        long idBusca = 3L;
        long tempoInicialBin = System.nanoTime();
        var p = passageiroServico.obterViaBuscaBinaria(idBusca);
        long tempoFinalBin = System.nanoTime();

        System.out.println("Resultado da busca ID " + idBusca + ": " + p);
        System.out.println("Tempo de execução (Busca Binária): " + (tempoFinalBin - tempoInicialBin) + " ns.");
    }

    // Huffman - Compressão de Dados
    public void executarModuloCompressao() {
        System.out.println("\n[Huffman] COMPRESSÃO DE HISTÓRICO DE MENSAGENS:");

        String textoOriginal = "USUARIO SOLICITOU VIAGEM. MOTORISTA ACEITOU. VIAGEM INICIADA EM SANTOS.";
        CompressaoServico compressaoServico = new CompressaoServico();

        long tempoInicial = System.nanoTime();
        ResultadoCompressao resultado = compressaoServico.comprimir(textoOriginal);
        long tempoFinal = System.nanoTime();

        String textoDescomprimido = compressaoServico.descomprimir(resultado);

        RelatorioCompressao relatorio = new RelatorioCompressao(
                textoOriginal,
                resultado.dadosComprimidos(),
                textoDescomprimido,
                (tempoFinal - tempoInicial)
        );

        System.out.println(relatorio);
    }
}