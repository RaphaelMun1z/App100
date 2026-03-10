package raphaelmun1z.servicos.grafo;

import raphaelmun1z.entidades.grafo.Aresta;
import raphaelmun1z.entidades.grafo.GrafoLista;
import raphaelmun1z.entidades.grafo.Rota;
import raphaelmun1z.entidades.interfaces.IGrafo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ==========================================
// ESTRUTURA: Grafo em Lista de Adjacência
// OBJETIVO: Mapear conexões entre cidades gastando o mínimo de memória possível.
// Diferente da Matriz (que cria espaços vazios para rotas que não existem), a Lista
// só aloca memória para as rotas reais que foram cadastradas. É a estrutura ideal
// para "Grafos Esparsos" (onde existem poucas conexões em relação ao total de cidades).
// ==========================================
public class GrafoListaService implements IGrafo {
    private final GrafoLista grafo;

    public GrafoListaService() {
        this.grafo = new GrafoLista();
    }

    @Override
    public void adicionarCidade(String nome) {
        // O Map funciona como um Dicionário.
        // A Chave (Key) é a Cidade de Origem. O Valor (Value) é a Lista de Caminhos.
        // O método 'putIfAbsent' faz o seguinte: "Se a cidade não existir, crie ela e dê
        // uma lista vazia de rotas. Se ela já existir, não faça NADA (para não apagar as rotas antigas)".
        grafo.getAdjList().putIfAbsent(nome, new ArrayList<>());
        IO.println("Cidade adicionada: " + nome);
    }

    @Override
    public void removerCidade(String nome) {
        Map<String, List<Aresta>> adj = grafo.getAdjList();

        // Passo 1: Remove o "Nó Principal".
        // Ex: Se apagarmos "Santos", a chave "Santos" e todas as rotas que saem dela somem instantaneamente.
        adj.remove(nome);

        // Passo 2: Limpeza de Lixo (Evitar pontas soltas).
        // Mesmo apagando "Santos" como origem, outras cidades (ex: São Vicente) ainda podem
        // ter rotas apontando PARA "Santos". Precisamos varrer o grafo inteiro apagando essas rotas mortas.
        for (List<Aresta> arestas : adj.values()) {
            // A expressão lambda 'removeIf' entra na lista de cada cidade vizinha e diz:
            // "Apague esta aresta se o destino final dela for a cidade que acabei de remover".
            arestas.removeIf(a -> a.getDestino().equals(nome));
        }
    }

    @Override
    public void adicionarRota(String origem, String destino, int peso, boolean bidirecional) {
        Map<String, List<Aresta>> adj = grafo.getAdjList();

        // Prevenção de erro: Um caminho só pode existir se os dois pontos existirem no mapa.
        if (adj.containsKey(origem) && adj.containsKey(destino)) {

            // Pega a lista de caminhos da cidade de Origem e adiciona uma nova Aresta (destino + custo/km).
            adj.get(origem).add(new Aresta(destino, peso));

            // Se for uma rua de mão dupla (bidirecional), fazemos o caminho reverso.
            // Pega a lista do Destino e adiciona uma Aresta voltando para a Origem.
            if (bidirecional) {
                adj.get(destino).add(new Aresta(origem, peso));
            }
        } else {
            IO.println("Erro: Uma das cidades não existe no mapa.");
        }
    }

    @Override
    public void removerRota(String origem, String destino, boolean bidirecional) {
        Map<String, List<Aresta>> adj = grafo.getAdjList();

        // Se a origem existe, entra na lista de arestas dela e remove apenas a que aponta para o destino.
        if (adj.containsKey(origem)) {
            adj.get(origem).removeIf(a -> a.getDestino().equals(destino));
        }

        // Se a rota for de mão dupla, entra no destino e corta a rota de volta para a origem.
        if (bidirecional && adj.containsKey(destino)) {
            adj.get(destino).removeIf(a -> a.getDestino().equals(origem));
        }
    }

    @Override
    public boolean temRota(String origem, String destino) {
        Map<String, List<Aresta>> adj = grafo.getAdjList();

        // O(1): Primeiro verificamos super rápido se a origem existe.
        if (!adj.containsKey(origem)) return false;

        // O(N): Depois, percorremos apenas os vizinhos diretos daquela origem específica.
        // Se encontrarmos o destino na lista, confirmamos que há conexão direta.
        for (Aresta a : adj.get(origem)) {
            if (a.getDestino().equals(destino)) return true;
        }
        return false;
    }

    @Override
    public void imprimirMapa() {
        IO.println("\n--- Mapa da Baixada (Lista) ---");

        // KeySet() pega todas as chaves (todas as cidades base).
        for (String cidade : grafo.getAdjList().keySet()) {
            System.out.print(cidade + " conecta com: ");

            // Imprime a lista completa de arestas daquela cidade
            System.out.println(grafo.getAdjList().get(cidade));
        }
    }

    // ==========================================
    // MÉTODOS TRADUTORES (Integração com outros algoritmos)
    // O DFS, BFS e Dijkstra não entendem a nossa estrutura crua de "Aresta".
    // Eles precisam do objeto de negócio "Rota". Esses métodos fazem essa conversão.
    // ==========================================
    @Override
    public List<Rota> obterRotasPartindoDe(String origem) {
        List<Rota> rotasDeSaida = new ArrayList<>();
        List<Aresta> arestas = grafo.getAdjList().get(origem);

        // Converte a Aresta interna do grafo para uma Rota genérica do sistema
        if (arestas != null) {
            for (Aresta aresta : arestas) {
                rotasDeSaida.add(new Rota(origem, aresta.getDestino(), aresta.getPeso()));
            }
        }
        return rotasDeSaida;
    }

    @Override
    public List<String> obterCidades() {
        // Retorna apenas a lista de chaves (Nós) cadastradas no Map, sem as arestas.
        return new ArrayList<>(grafo.getAdjList().keySet());
    }

    @Override
    public Map<String, List<Rota>> obterTodasAdjacencias() {
        Map<String, List<Rota>> todasAdjacencias = new HashMap<>();

        // Cria uma cópia inteira do grafo, mas convertida para o formato genérico de Rotas.
        // Útil para enviar o "snapshot" do mapa para a interface ou para o Ordenador Topológico.
        for (String cidade : grafo.getAdjList().keySet()) {
            todasAdjacencias.put(cidade, obterRotasPartindoDe(cidade));
        }
        return todasAdjacencias;
    }
}