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

// ==========================================
// CAMADA DE ORQUESTRAÇÃO E DEMONSTRAÇÃO (Test Runner)
// OBJETIVO: Servir como um ambiente de testes práticos para acionar e validar
// os algoritmos isolados, exibindo métricas de desempenho (tempo de execução).
// ==========================================
public class ModulosGeraisController {

    // Injeção dos serviços necessários para rodar as demonstrações
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

    // ==========================================
    // DEMONSTRAÇÃO 1: RABIN-KARP NA PRÁTICA
    // Simula uma troca de mensagens real para provar que o filtro intercepta
    // as palavras proibidas antes de consolidar o histórico.
    // ==========================================
    public void executarModuloChat() {
        System.out.println("\n[Rabin Karp] MONITORAMENTO DE CHAT:");

        // Prepara o cenário: resgata usuários mockados e inicia uma sala de chat
        Motorista m1 = motoristaServico.obterTodos().getFirst();
        Passageiro p1 = passageiroServico.obterTodos().getFirst();
        Chat chat1 = new Chat(1L, m1, p1);

        // Criação de cenários de teste: uma mensagem limpa e uma contendo gatilhos
        Mensagem msg1 = new Mensagem(1L, p1, m1, "Bom dia! Chego em 5 min.");
        Mensagem msg2 = new Mensagem(2L, p1, m1, "Teste de sistema: golpe e fraude 123");

        // Envia mensagem normal (não deve ser barrada)
        chatServico.enviarMensagem(chat1, msg1);

        // Envia mensagem maliciosa e faz o benchmark (medição de tempo) do algoritmo
        // O nanoTime() é crucial aqui para provar que o Rolling Hash do Rabin-Karp
        // é rápido o suficiente para não causar lentidão na experiência do usuário.
        long tempoInicialRK = System.nanoTime();
        chatServico.enviarMensagem(chat1, msg2);
        long tempoFinalRK = System.nanoTime();

        // Exibe o resultado final provando que a msg2 foi censurada
        chatServico.imprimeMensagens(chat1);
        System.out.println("Tempo de verificação (Rabin Karp): " + (tempoFinalRK - tempoInicialRK) + " nanossegundos.");
    }

    // ==========================================
    // DEMONSTRAÇÃO 2: ALGORITMOS DE BUSCA
    // Executa e compara explicitamente as estratégias de busca implementadas.
    // ==========================================
    public void executarModuloBusca() {
        System.out.println("\n[Busca Sequencial vs Busca Binária] Demonstração de Performance:");

        // 1. Aciona a busca sequencial e mede sua performance.
        long idBuscaSeq = 3L;
        long tempoInicialSeq = System.nanoTime();
        var tipoCorrida = tipoCorridaServico.obterViaIdBuscaSequencial(idBuscaSeq);
        long tempoFinalSeq = System.nanoTime();

        System.out.println("\n>>> BUSCA SEQUENCIAL (Tipos de Corrida)");
        System.out.println("Resultado da busca ID " + idBuscaSeq + ": " + tipoCorrida);
        System.out.println("Tempo de execução: " + (tempoFinalSeq - tempoInicialSeq) + " ns.");

        // 2. Aciona a busca binária e mede sua performance.
        // Em um cenário com milhões de registros, a diferença de tempo a favor
        // da busca binária seria ainda mais drástica em relação à sequencial.
        long idBuscaBin = 3L;
        long tempoInicialBin = System.nanoTime();
        var p = passageiroServico.obterViaBuscaBinaria(idBuscaBin);
        long tempoFinalBin = System.nanoTime();

        System.out.println("\n>>> BUSCA BINÁRIA (Passageiros)");
        System.out.println("Resultado da busca ID " + idBuscaBin + ": " + p);
        System.out.println("Tempo de execução: " + (tempoFinalBin - tempoInicialBin) + " ns.");
    }

    // ==========================================
    // DEMONSTRAÇÃO 3: COMPRESSÃO DE HUFFMAN
    // Simula a compactação de um log de sistema ou histórico de rotas,
    // um caso de uso real para economizar espaço em banco de dados ou banda de rede.
    // ==========================================
    public void executarModuloCompressao() {
        System.out.println("\n[Huffman] COMPRESSÃO DE HISTÓRICO DE MENSAGENS:");

        String textoOriginal = "USUARIO SOLICITOU VIAGEM. MOTORISTA ACEITOU. VIAGEM INICIADA EM SANTOS.";
        CompressaoServico compressaoServico = new CompressaoServico();

        // Faz o benchmark de todo o ciclo de vida da compressão
        // (montar mapa de frequência, construir árvore, gerar códigos e substituir texto).
        long tempoInicial = System.nanoTime();
        ResultadoCompressao resultado = compressaoServico.comprimir(textoOriginal);
        long tempoFinal = System.nanoTime();

        // Validação de integridade: garante que reverter o processo devolve
        // exatamente a string original, sem perda de dados (Lossless compression).
        String textoDescomprimido = compressaoServico.descomprimir(resultado);

        // Agrupa as métricas brutas em um objeto Relatorio para facilitar a exibição visual
        RelatorioCompressao relatorio = new RelatorioCompressao(
                textoOriginal,
                resultado.dadosComprimidos(),
                textoDescomprimido,
                (tempoFinal - tempoInicial)
        );

        System.out.println(relatorio);
    }
}