package raphaelmun1z.servicos.grafo;

import raphaelmun1z.entidades.grafo.Aresta;
import raphaelmun1z.entidades.grafo.GrafoLista;
import raphaelmun1z.entidades.grafo.Rota;
import raphaelmun1z.entidades.interfaces.IGrafo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoListaService implements IGrafo {
    private final GrafoLista grafo;

    public GrafoListaService() {
        this.grafo = new GrafoLista();
    }

    @Override
    public void adicionarCidade(String nome) {
        grafo.getAdjList().putIfAbsent(nome, new ArrayList<>());
        IO.println("Cidade adicionada: " + nome);
    }

    @Override
    public void removerCidade(String nome) {
        Map<String, List<Aresta>> adj = grafo.getAdjList();
        adj.remove(nome);

        for (List<Aresta> arestas : adj.values()) {
            arestas.removeIf(a -> a.getDestino().equals(nome));
        }
    }

    @Override
    public void adicionarRota(String origem, String destino, int peso, boolean bidirecional) {
        Map<String, List<Aresta>> adj = grafo.getAdjList();

        if (adj.containsKey(origem) && adj.containsKey(destino)) {
            adj.get(origem).add(new Aresta(destino, peso));

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

        if (adj.containsKey(origem)) {
            adj.get(origem).removeIf(a -> a.getDestino().equals(destino));
        }
        if (bidirecional && adj.containsKey(destino)) {
            adj.get(destino).removeIf(a -> a.getDestino().equals(origem));
        }
    }

    @Override
    public boolean temRota(String origem, String destino) {
        Map<String, List<Aresta>> adj = grafo.getAdjList();

        if (!adj.containsKey(origem)) return false;

        for (Aresta a : adj.get(origem)) {
            if (a.getDestino().equals(destino)) return true;
        }
        return false;
    }

    @Override
    public void imprimirMapa() {
        IO.println("\n--- Mapa da Baixada (Lista) ---");
        for (String cidade : grafo.getAdjList().keySet()) {
            System.out.print(cidade + " conecta com: ");
            System.out.println(grafo.getAdjList().get(cidade));
        }
    }

    @Override
    public List<Rota> obterRotasPartindoDe(String origem) {
        List<Rota> rotasDeSaida = new ArrayList<>();
        List<Aresta> arestas = grafo.getAdjList().get(origem);

        if (arestas != null) {
            for (Aresta aresta : arestas) {
                rotasDeSaida.add(new Rota(origem, aresta.getDestino(), aresta.getPeso()));
            }
        }
        return rotasDeSaida;
    }

    @Override
    public List<String> obterCidades() {
        return new ArrayList<>(grafo.getAdjList().keySet());
    }

    @Override
    public Map<String, List<Rota>> obterTodasAdjacencias() {
        Map<String, List<Rota>> todasAdjacencias = new HashMap<>();

        for (String cidade : grafo.getAdjList().keySet()) {
            todasAdjacencias.put(cidade, obterRotasPartindoDe(cidade));
        }
        return todasAdjacencias;
    }
}
