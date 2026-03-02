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

public class Main {
    private static MotoristaServico motoristaServico;
    private static PassageiroServico passageiroServico;
    private static TipoCorridaServico tipoCorridaServico;
    private static ChatServico chatServico;

    private static InputHandler inputHandler;
    private static final Scanner scanner = new Scanner(System.in);

    static void main() {
        inicializarServicos();

        CorrecaoEnderecoServico pdServico = new CorrecaoEnderecoServico();
        inputHandler = new InputHandler(scanner, pdServico);

        ModulosGeraisController modulosGerais = new ModulosGeraisController(
                motoristaServico, passageiroServico, chatServico, tipoCorridaServico
        );

        // 4. Configuração do Menu de Grafos (Submenu)
        Menu menuGrafos = new Menu("MÓDULO DE GRAFOS (MAPAS)");

        menuGrafos.adicionarOpcao(1, "Matriz de Adjacência", () -> {
            IGrafo matriz = new GrafoMatrizService(10);
            inicializarCenarioBaixada(matriz);
            new GrafoController(matriz, scanner, inputHandler).exibirMenu("Matriz");
        });

        menuGrafos.adicionarOpcao(2, "Lista de Adjacência", () -> {
            IGrafo lista = new GrafoListaService();
            inicializarCenarioBaixada(lista);
            new GrafoController(lista, scanner, inputHandler).exibirMenu("Lista");
        });

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

        // 5. Menu Principal
        Menu menuPrincipal = new Menu("APP 100 - MOBILIDADE URBANA");

        menuPrincipal.adicionarOpcao(1, "(Rabin-Karp) Monitoramento de Chat",
                modulosGerais::executarModuloChat);

        menuPrincipal.adicionarOpcao(2, "(Busca) Tipos de Corrida e Passageiros",
                modulosGerais::executarModuloBusca);

        menuPrincipal.adicionarOpcao(3, "(Huffman) Compressão de Histórico",
                modulosGerais::executarModuloCompressao);

        menuPrincipal.adicionarOpcao(4, "(Grafos & PD) Mapas e Rotas Corrigidas",
                () -> menuGrafos.executar(scanner));

        // Execução do ‘loop’ principal
        menuPrincipal.executar(scanner);
        scanner.close();
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
}