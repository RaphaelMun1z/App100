package raphaelmun1z.ui;

import raphaelmun1z.entidades.grafo.Rota;
import raphaelmun1z.entidades.interfaces.IGrafo;
import raphaelmun1z.servicos.grafo.GrafoListaService;
import raphaelmun1z.servicos.grafo.GrafoOtimizacaoService;
import raphaelmun1z.entidades.sistema.Menu;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GrafoController {
    private final IGrafo grafo;
    private final Scanner scanner;
    private final InputHandler input;
    private final GrafoOtimizacaoService otimizacaoServico;

    public GrafoController(IGrafo grafo, Scanner scanner, InputHandler input) {
        this.grafo = grafo;
        this.scanner = scanner;
        this.input = input;
        this.otimizacaoServico = new GrafoOtimizacaoService();
    }

    public void exibirMenu(String tipo) {
        Menu menuAcoes = new Menu("MAPA DE GRAFOS (" + tipo + ")");

        menuAcoes.adicionarOpcao(1, "Visualizar Mapa", grafo::imprimirMapa);
        menuAcoes.adicionarOpcao(2, "Gerenciar Cidades/Rotas (CRUD)", this::exibirMenuCRUD);
        menuAcoes.adicionarOpcao(3, "(DFS) Exploração de Rotas", this::uiBuscaDFS);
        menuAcoes.adicionarOpcao(4, "(BFS) Alcance de Notificação", this::uiBuscaBFS);
        menuAcoes.adicionarOpcao(5, "(Dijkstra) Rota Mais Rápida", this::uiDijkstra);
        menuAcoes.adicionarOpcao(6, "(AGM) Otimizar Rede de Abastecimento", this::uiAGM);
        menuAcoes.adicionarOpcao(7, "(Topológica) Sequenciar Manutenção de Frota", this::uiTopologica);

        menuAcoes.executar(scanner);
    }

    private void exibirMenuCRUD() {
        Menu menuCrud = new Menu("GERENCIAR MAPA");

        menuCrud.adicionarOpcao(1, "Visualizar Mapa", grafo::imprimirMapa);
        menuCrud.adicionarOpcao(2, "Adicionar Cidade", this::uiAdicionarCidade);
        menuCrud.adicionarOpcao(3, "Remover Cidade", this::uiRemoverCidade);
        menuCrud.adicionarOpcao(4, "Adicionar Rota", this::uiAdicionarRota);
        menuCrud.adicionarOpcao(5, "Remover Rota", this::uiRemoverRota);
        menuCrud.adicionarOpcao(6, "Verificar Conexão", this::uiVerificarRota);

        menuCrud.executar(scanner);
    }

    private void uiDijkstra() {
        String origem = input.lerCidadeValidada("Origem (Viagem Inicia): ", grafo);
        String destino = input.lerCidadeValidada("Destino (Viagem Termina): ", grafo);

        List<String> caminho = otimizacaoServico.dijkstra(grafo, origem, destino);

        System.out.println("\n[DIJKSTRA] Rota de Menor Custo (Mais Rápida):");
        if (caminho.isEmpty()) {
            System.out.println("Rota não encontrada ou destino inalcançável.");
        } else {
            System.out.println("Caminho Otimizado: " + String.join(" -> ", caminho));
        }
    }

    private void uiBuscaDFS() {
        String origem = input.lerCidadeValidada("Origem da Exploração: ", grafo);
        String alvo = input.lerCidadeValidada("Cidade Alvo: ", grafo);

        List<String> caminho = otimizacaoServico.buscaProfundidadeDFS(grafo, origem, alvo);

        System.out.println("\n[DFS] Caminho Encontrado:");
        if (caminho.isEmpty()) {
            System.out.println("Caminho não encontrado ou alvo inalcançável.");
        } else {
            System.out.println("Caminho percorrido: " + String.join(" -> ", caminho));
        }
    }

    private void uiBuscaBFS() {
        String origem = input.lerCidadeValidada("Origem (Notícia Inicia): ", grafo);

        Map<String, Integer> distancias = otimizacaoServico.buscaLarguraBFS(grafo, origem);

        System.out.println("\n[BFS] Distância de Conexão (Propagação):");
        distancias.forEach((cidade, dist) -> {
            if (dist != Integer.MAX_VALUE) {
                System.out.println("- " + cidade + ": " + dist + " conexões de distância.");
            } else {
                System.out.println("- " + cidade + ": Inalcançável.");
            }
        });
    }

    private void uiAGM() {
        String inicio = input.lerCidadeValidada("Ponto de Início da Rede: ", grafo);

        List<Rota> agm = otimizacaoServico.arvoreGeradoraMinimaAGM(grafo, inicio);
        long custoTotal = agm.stream().mapToLong(Rota::getPeso).sum();

        System.out.println("\n[AGM] Otimização de Infraestrutura:");
        if (agm.isEmpty() && grafo.obterCidades().size() > 1) {
            System.out.println("AVISO: AGM não encontrada. O grafo pode ser desconexo.");
        } else {
            for (Rota rota : agm) {
                System.out.println("- " + rota.getOrigem() + " -> " + rota.getDestino() + " (Custo: " + rota.getPeso() + ")");
            }
            System.out.println("CUSTO TOTAL DA INFRAESTRUTURA OTIMIZADA: " + custoTotal);
        }
    }

    private void uiTopologica() {
        IGrafo grafoDAG = new GrafoListaService();
        grafoDAG.adicionarCidade("Verificar Pneus");
        grafoDAG.adicionarCidade("Trocar Oleo");
        grafoDAG.adicionarCidade("Teste de Freios");
        grafoDAG.adicionarCidade("Limpar Veiculo");
        grafoDAG.adicionarCidade("Liberar para Rodar");

        grafoDAG.adicionarRota("Verificar Pneus", "Teste de Freios", 1, false);
        grafoDAG.adicionarRota("Trocar Oleo", "Teste de Freios", 1, false);
        grafoDAG.adicionarRota("Teste de Freios", "Limpar Veiculo", 1, false);
        grafoDAG.adicionarRota("Limpar Veiculo", "Liberar para Rodar", 1, false);

        List<String> ordem = otimizacaoServico.ordenacaoTopologica(grafoDAG);

        System.out.println("\n[ORDENAÇÃO TOPOLÓGICA] Sequência de Tarefas de Frota:");
        if (ordem.isEmpty()) {
            System.out.println("Não foi possível gerar a sequência (Possível Ciclo).");
        } else {
            System.out.println("Sequência: " + String.join(" -> ", ordem));
        }
    }

    private void uiAdicionarCidade() {
        System.out.print("Nome da nova cidade: ");
        grafo.adicionarCidade(scanner.nextLine());
    }

    private void uiRemoverCidade() {
        String nome = input.lerCidadeValidada("Cidade para remover: ", grafo);
        grafo.removerCidade(nome);
    }

    private void uiAdicionarRota() {
        try {
            String origem = input.lerCidadeValidada("Origem: ", grafo);
            String destino = input.lerCidadeValidada("Destino: ", grafo);

            System.out.print("Peso (Distância/Custo): ");
            int peso = Integer.parseInt(scanner.nextLine());

            System.out.print("É mão dupla? (S/N): ");
            boolean bidirecional = scanner.nextLine().equalsIgnoreCase("S");

            grafo.adicionarRota(origem, destino, peso, bidirecional);
            System.out.println("Rota adicionada!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Peso deve ser um número inteiro.");
        }
    }

    private void uiRemoverRota() {
        String origem = input.lerCidadeValidada("Origem: ", grafo);
        String destino = input.lerCidadeValidada("Destino: ", grafo);

        System.out.print("Remover volta também? (S/N): ");
        boolean bidirecional = scanner.nextLine().equalsIgnoreCase("S");

        grafo.removerRota(origem, destino, bidirecional);
        System.out.println("Rota removida.");
    }

    private void uiVerificarRota() {
        String origem = input.lerCidadeValidada("Origem: ", grafo);
        String destino = input.lerCidadeValidada("Destino: ", grafo);

        boolean existe = grafo.temRota(origem, destino);
        System.out.println(existe ? ">>> EXISTE rota direta." : ">>> NÃO existe rota direta.");
    }
}