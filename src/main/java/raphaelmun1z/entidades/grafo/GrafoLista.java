package raphaelmun1z.entidades.grafo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoLista {
    private Map<String, List<Aresta>> adjList;

    public GrafoLista() {
        this.adjList = new HashMap<>();
    }

    public Map<String, List<Aresta>> getAdjList() {
        return adjList;
    }

    public void setAdjList(Map<String, List<Aresta>> adjList) {
        this.adjList = adjList;
    }
}