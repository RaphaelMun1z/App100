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
import raphaelmun1z.servicos.grafo.GrafoListaService;
import raphaelmun1z.servicos.grafo.GrafoMatrizService;
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
        menuGrafos.adicionarOpcao(1, "Matriz de Adjacência", () -> {
            IGrafo matriz = new GrafoMatrizService(10);
            inicializarCenarioBaixada(matriz);
            executarMenuCRUD(matriz, scanner, "Matriz");
        });

        menuGrafos.adicionarOpcao(2, "Lista de Adjacência", () -> {
            IGrafo lista = new GrafoListaService();
            inicializarCenarioBaixada(lista);
            executarMenuCRUD(lista, scanner, "Lista");
        });

        menuGrafos.adicionarOpcao(3, "Comparar Implementações", () -> {
            IO.println("\n>>> Comparando Implementações...");
            IGrafo m = new GrafoMatrizService(10);
            IGrafo l = new GrafoListaService();

            IO.println("--- MATRIZ ---");
            inicializarCenarioBaixada(m);
            m.imprimirMapa();

            IO.println("--- LISTA ---");
            inicializarCenarioBaixada(l);
            l.imprimirMapa();
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

    private static void executarMenuCRUD(IGrafo grafo, Scanner scanner, String tipo) {
        Menu menuCrud = new Menu("GERENCIAR MAPA (" + tipo + ")");

        menuCrud.adicionarOpcao(1, "Visualizar Mapa", grafo::imprimirMapa);
        menuCrud.adicionarOpcao(2, "Adicionar Cidade", () -> uiAdicionarCidade(grafo, scanner));
        menuCrud.adicionarOpcao(3, "Remover Cidade", () -> uiRemoverCidade(grafo, scanner));
        menuCrud.adicionarOpcao(4, "Adicionar Rota", () -> uiAdicionarRota(grafo, scanner));
        menuCrud.adicionarOpcao(5, "Remover Rota", () -> uiRemoverRota(grafo, scanner));
        menuCrud.adicionarOpcao(6, "Verificar Conexão", () -> uiVerificarRota(grafo, scanner));

        menuCrud.executar(scanner);
    }

    private static void uiAdicionarCidade(IGrafo grafo, Scanner scanner) {
        System.out.print("Nome da nova cidade: ");
        String nome = scanner.nextLine();
        grafo.adicionarCidade(nome);
    }

    private static void uiRemoverCidade(IGrafo grafo, Scanner scanner) {
        System.out.print("Nome da cidade para remover: ");
        String nome = scanner.nextLine();
        grafo.removerCidade(nome);
    }

    private static void uiAdicionarRota(IGrafo grafo, Scanner scanner) {
        try {
            System.out.print("Origem: ");
            String origem = scanner.nextLine();
            System.out.print("Destino: ");
            String destino = scanner.nextLine();

            System.out.print("Peso (Distância/Custo): ");
            int peso = Integer.parseInt(scanner.nextLine());

            System.out.print("É mão dupla? (S/N): ");
            String resp = scanner.nextLine();
            boolean bidirecional = resp.equalsIgnoreCase("S");

            grafo.adicionarRota(origem, destino, peso, bidirecional);
            IO.println("Rota adicionada/atualizada!");
        } catch (NumberFormatException e) {
            IO.println("Erro: Peso deve ser um número inteiro.");
        }
    }

    private static void uiRemoverRota(IGrafo grafo, Scanner scanner) {
        System.out.print("Origem: ");
        String origem = scanner.nextLine();
        System.out.print("Destino: ");
        String destino = scanner.nextLine();

        System.out.print("Remover volta também? (S/N): ");
        boolean bidirecional = scanner.nextLine().equalsIgnoreCase("S");

        grafo.removerRota(origem, destino, bidirecional);
        IO.println("Rota removida.");
    }

    private static void uiVerificarRota(IGrafo grafo, Scanner scanner) {
        System.out.print("Origem: ");
        String origem = scanner.nextLine();
        System.out.print("Destino: ");
        String destino = scanner.nextLine();

        boolean existe = grafo.temRota(origem, destino);
        IO.println(existe ? ">>> EXISTE uma rota direta." : ">>> NÃO existe rota direta.");
    }

    private static void inicializarCenarioBaixada(IGrafo mapa) {
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