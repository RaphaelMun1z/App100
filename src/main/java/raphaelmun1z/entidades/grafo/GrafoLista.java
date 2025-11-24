package raphaelmun1z.entidades.grafo;

import raphaelmun1z.entidades.interfaces.IGrafo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoLista implements IGrafo {
    private Map<String, List<Aresta>> adjList;

    public GrafoLista() {
        this.adjList = new HashMap<>();
    }

    @Override
    public void adicionarCidade(String nome) {
        adjList.putIfAbsent(nome, new ArrayList<>());
        System.out.println("Cidade adicionada: " + nome);
    }

    @Override
    public void removerCidade(String nome) {
        adjList.remove(nome);
        for (List<Aresta> arestas : adjList.values()) {
            arestas.removeIf(a -> a.destino.equals(nome));
        }
    }

    @Override
    public void adicionarRota(String origem, String destino, int peso, boolean bidirecional) {
        if (adjList.containsKey(origem) && adjList.containsKey(destino)) {
            adjList.get(origem).add(new Aresta(destino, peso));
            if (bidirecional) {
                adjList.get(destino).add(new Aresta(origem, peso));
            }
        }
    }

    @Override
    public void removerRota(String origem, String destino, boolean bidirecional) {
        if (adjList.containsKey(origem)) {
            adjList.get(origem).removeIf(a -> a.destino.equals(destino));
        }
        if (bidirecional && adjList.containsKey(destino)) {
            adjList.get(destino).removeIf(a -> a.destino.equals(origem));
        }
    }

    @Override
    public boolean temRota(String origem, String destino) {
        if (!adjList.containsKey(origem)) return false;
        for (Aresta a : adjList.get(origem)) {
            if (a.destino.equals(destino)) return true;
        }
        return false;
    }

    @Override
    public void imprimirMapa() {
        System.out.println("\n--- Mapa da Baixada (Lista) ---");
        for (String cidade : adjList.keySet()) {
            System.out.print(cidade + " conecta com: ");
            System.out.println(adjList.get(cidade));
        }
    }
}