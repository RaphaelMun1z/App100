package raphaelmun1z;

import raphaelmun1z.entidades.interfaces.IGrafo;
import raphaelmun1z.entidades.sistema.Menu;
import raphaelmun1z.servicos.busca.TipoCorridaServico;
import raphaelmun1z.servicos.grafo.GrafoListaService;
import raphaelmun1z.servicos.grafo.GrafoMatrizService;
import raphaelmun1z.servicos.pd.CorrecaoEnderecoServico;
import raphaelmun1z.servicos.rabinKarp.ChatServico;
import raphaelmun1z.servicos.usuario.MotoristaServico;
import raphaelmun1z.servicos.usuario.PassageiroServico;
import raphaelmun1z.ui.GrafoController;
import raphaelmun1z.ui.InputHandler;
import raphaelmun1z.ui.ModulosGeraisController;

import java.util.Scanner;

// ==========================================
// CLASSE ORQUESTRADORA (MAIN)
// OBJETIVO: Ponto de entrada da aplicação. Inicializa os serviços, injeta as dependências
// e monta a interface de linha de comando (CLI) que conecta o usuário aos algoritmos.
// ==========================================
public class Main {

    // Instâncias globais dos serviços que atuam como nossos "bancos de dados" em memória
    private static MotoristaServico motoristaServico;
    private static PassageiroServico passageiroServico;
    private static TipoCorridaServico tipoCorridaServico;
    private static ChatServico chatServico;

    private static InputHandler inputHandler;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // 1. Popula o sistema com a massa de dados fictícia necessária para os testes
        inicializarServicos();

        // 2. Prepara o serviço de Programação Dinâmica (Levenshtein) para atuar como
        // filtro corretor das entradas do usuário no terminal
        CorrecaoEnderecoServico pdServico = new CorrecaoEnderecoServico();
        inputHandler = new InputHandler(scanner, pdServico);

        // 3. Centraliza os módulos independentes (Busca, Huffman, Rabin-Karp) em um controlador
        ModulosGeraisController modulosGerais = new ModulosGeraisController(
                motoristaServico, passageiroServico, chatServico, tipoCorridaServico
        );

        // ==========================================
        // SUBMENU DE GRAFOS: APLICAÇÃO DE POLIMORFISMO
        // ==========================================
        Menu menuGrafos = new Menu("MÓDULO DE GRAFOS (MAPAS)");

        // Demonstra a injeção da implementação baseada em MATRIZ
        menuGrafos.adicionarOpcao(1, "Matriz de Adjacência", () -> {
            // Repare: Declaramos como IGrafo (Interface), mas instanciamos a Matriz.
            IGrafo matriz = new GrafoMatrizService(10);
            inicializarCenarioBaixada(matriz);
            new GrafoController(matriz, scanner, inputHandler).exibirMenu("Matriz");
        });

        // Demonstra a injeção da implementação baseada em LISTA
        menuGrafos.adicionarOpcao(2, "Lista de Adjacência", () -> {
            // O mesmo controlador (GrafoController) aceita a Lista naturalmente.
            // Isso garante um código escalável e de fácil manutenção.
            IGrafo lista = new GrafoListaService();
            inicializarCenarioBaixada(lista);
            new GrafoController(lista, scanner, inputHandler).exibirMenu("Lista");
        });

        // Permite visualizar lado a lado como cada estrutura de dados aloca
        // as mesmas informações topológicas na memória
        menuGrafos.adicionarOpcao(3, "Comparar Implementações", () -> {
            System.out.println("\n>>> Comparando Implementações...");
            IGrafo m = new GrafoMatrizService(10);
            IGrafo l = new GrafoListaService();
            inicializarCenarioBaixada(m);
            inicializarCenarioBaixada(l);

            System.out.println("--- MATRIZ ---");
            m.imprimirMapa();

            System.out.println("--- LISTA ---");
            l.imprimirMapa();
        });

        // ==========================================
        // MENU PRINCIPAL (APP 100)
        // Agrupa todas as disciplinas de Estrutura de Dados 2 em casos de uso de negócio
        // ==========================================
        Menu menuPrincipal = new Menu("APP 100 - MOBILIDADE URBANA");

        menuPrincipal.adicionarOpcao(1, "(Rabin-Karp) Monitoramento de Chat",
                modulosGerais::executarModuloChat);

        menuPrincipal.adicionarOpcao(2, "(Busca) Tipos de Corrida e Passageiros",
                modulosGerais::executarModuloBusca);

        menuPrincipal.adicionarOpcao(3, "(Huffman) Compressão de Histórico",
                modulosGerais::executarModuloCompressao);

        menuPrincipal.adicionarOpcao(4, "(Grafos & PD) Mapas e Rotas Corrigidas",
                () -> menuGrafos.executar(scanner));

        // Inicia o ciclo de vida da aplicação retendo o terminal
        menuPrincipal.executar(scanner);
        scanner.close();
    }

    // ==========================================
    // CENÁRIO DE TESTE: MAPA DA BAIXADA SANTISTA
    // Popula o grafo abstrato com um mapa real conhecido para facilitar
    // a visualização do Dijkstra, DFS e AGM durante a apresentação.
    // ==========================================
    //
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

    // Instancia os serviços e insere os registros simulando o que
    // viria de um banco de dados relacional.
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
}