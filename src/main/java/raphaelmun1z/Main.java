package raphaelmun1z;

import raphaelmun1z.dto.ResultadoCompressao;
import raphaelmun1z.entidades.huffman.RelatorioCompressao;
import raphaelmun1z.entidades.rabinKarp.Chat;
import raphaelmun1z.entidades.grafo.GrafoLista;
import raphaelmun1z.entidades.grafo.GrafoMatriz;
import raphaelmun1z.entidades.rabinKarp.Mensagem;
import raphaelmun1z.entidades.sistema.Menu;
import raphaelmun1z.entidades.usuario.Motorista;
import raphaelmun1z.entidades.usuario.Passageiro;
import raphaelmun1z.entidades.interfaces.IGrafo;
import raphaelmun1z.servicos.busca.TipoCorridaServico;
import raphaelmun1z.servicos.huffman.CompressaoServico;
import raphaelmun1z.servicos.rabinKarp.ChatServico;
import raphaelmun1z.servicos.usuario.MotoristaServico;
import raphaelmun1z.servicos.usuario.PassageiroServico;

import java.util.Scanner;

public class Main {
    private static MotoristaServico motoristaServico;
    private static PassageiroServico passageiroServico;
    private static TipoCorridaServico tipoCorridaServico;
    private static ChatServico chatServico;

    static void main() {
        inicializarServicos();
        Scanner scanner = new Scanner(System.in);

        Menu menuGrafos = new Menu("MÓDULO DE GRAFOS (MAPAS)");
        menuGrafos.adicionarOpcao(1, "Testar Matriz de Adjacência",
            () -> rodarCenarioBaixada(new GrafoMatriz(10), "Matriz"));

        menuGrafos.adicionarOpcao(2, "Testar Lista de Adjacência",
            () -> rodarCenarioBaixada(new GrafoLista(), "Lista"));

        menuGrafos.adicionarOpcao(3, "Comparar Ambas", () -> {
            IO.println("\n>>> Comparando Implementações...");
            rodarCenarioBaixada(new GrafoMatriz(10), "Matriz");
            IO.println("-----------------------------------");
            rodarCenarioBaixada(new GrafoLista(), "Lista");
        });

        Menu menuPrincipal = new Menu("APP 100 - MENU PRINCIPAL");
        menuPrincipal.adicionarOpcao(1, "(Rabin-Karp) Chat",
            Main::executarModuloChat);

        menuPrincipal.adicionarOpcao(2, "(Busca) Tipos de Corrida e Passageiros",
            Main::executarModuloBusca);

        menuPrincipal.adicionarOpcao(3, "(Huffman) Compressão de Mensagem",
            Main::executarModuloCompressao);

        menuPrincipal.adicionarOpcao(4, "(Grafos) Mapas e Rotas",
            () -> menuGrafos.executar(scanner));

        menuPrincipal.executar(scanner);
        scanner.close();
    }

    private static void inicializarServicos() {
        motoristaServico = new MotoristaServico();
        passageiroServico = new PassageiroServico();
        tipoCorridaServico = new TipoCorridaServico();
        chatServico = new ChatServico();

        motoristaServico.mockDados();
        passageiroServico.mockDados();
        tipoCorridaServico.mockDados();
        chatServico.mockDados();
    }

    // Rabin Karp
    private static void executarModuloChat() {
        IO.println("\n[Rabin Karp] CHAT:");

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
        IO.println("Tempo de verificação (Rabin Karp): " + (tempoFinalRK - tempoInicialRK) + " nanossegundos.");
    }

    // Busca Sequencial e Binária
    private static void executarModuloBusca() {
        IO.println("\n[Busca Sequencial] Listar Tipos de Corrida");
        IO.println("\nTODOS:");
        tipoCorridaServico.obterTodos().forEach(IO::println);

        IO.println("\nID 3:");
        long tempoInicialSeq = System.nanoTime();
        IO.println(tipoCorridaServico.obterViaIdBuscaSequencial(3L));
        long tempoFinalSeq = System.nanoTime();

        IO.println("Tempo de busca sequencial: " + (tempoFinalSeq - tempoInicialSeq) + " nanossegundos.");

        IO.println("\n_________________________________");

        IO.println("\n[Busca Binária] Listar Passageiros");
        IO.println("\nTODOS:");
        passageiroServico.obterTodos().forEach(IO::println);

        IO.println("\nID 3:");
        long tempoInicialBin = System.nanoTime();
        IO.println(passageiroServico.obterViaBuscaBinaria(3L));
        long tempoFinalBin = System.nanoTime();

        IO.println("Tempo de busca binária: " + (tempoFinalBin - tempoInicialBin) + " nanossegundos.");
    }

    // Huffman
    private static void executarModuloCompressao() {
        IO.println("\n[Huffman] Compressão\n");

        String textoOriginal = "TESTE DE COMPRESSAO PARA TESTAR O ALGORITMO DE HUFFMAN. TESTE COM REPETICAO.";
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

        IO.println(relatorio);
    }

    // Grafos
    private static void rodarCenarioBaixada(IGrafo mapa, String nomeImplementacao) {
        IO.println("\n[Grafo] " + nomeImplementacao + "\n");

        mapa.adicionarCidade("Santos");
        mapa.adicionarCidade("SV");
        mapa.adicionarCidade("PG");
        mapa.adicionarCidade("Guaruja");
        mapa.adicionarCidade("Cubatao");

        mapa.adicionarRota("Santos", "SV", 5, true);
        mapa.adicionarRota("SV", "PG", 8, true);
        mapa.adicionarRota("Santos", "Guaruja", 20, true);
        mapa.adicionarRota("Santos", "Cubatao", 15, true);
        mapa.adicionarRota("Cubatao", "SV", 12, true);

        mapa.imprimirMapa();

        IO.println("Tem rota Santos -> PG? " + (mapa.temRota("Santos", "PG") ? "Sim" : "Não"));

        IO.println("Removendo rota Santos <-> Guarujá...");
        mapa.removerRota("Santos", "Guaruja", true);

        mapa.imprimirMapa();
    }
}