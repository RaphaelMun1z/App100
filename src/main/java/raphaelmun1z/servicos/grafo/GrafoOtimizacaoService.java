package raphaelmun1z.servicos.grafo;

import raphaelmun1z.entidades.grafo.Rota;
import raphaelmun1z.entidades.interfaces.IGrafo;

import java.util.*;

public class GrafoOtimizacaoService {
    public List<String> buscaProfundidadeDFS(IGrafo grafo, String inicio, String alvo) {
        Set<String> visitados = new HashSet<>();
        Stack<String> pilha = new Stack<>();
        Map<String, String> pai = new HashMap<>();

        pilha.push(inicio);
        visitados.add(inicio);

        while (!pilha.isEmpty()) {
            String atual = pilha.pop();

            if (atual.equals(alvo)) {
                return reconstruirCaminho(pai, inicio, alvo);
            }

            List<Rota> rotas = grafo.obterRotasPartindoDe(atual);
            if (rotas == null) continue;

            for (Rota rota : rotas) {
                String vizinho = rota.getDestino();
                if (!visitados.contains(vizinho)) {
                    visitados.add(vizinho);
                    pilha.push(vizinho);
                    pai.put(vizinho, atual);
                }
            }
        }
        return Collections.emptyList();
    }

    public Map<String, Integer> buscaLarguraBFS(IGrafo grafo, String inicio) {
        Map<String, Integer> distancias = new HashMap<>();
        Queue<String> fila = new LinkedList<>();

        for (String cidade : grafo.obterCidades()) {
            distancias.put(cidade, Integer.MAX_VALUE);
        }

        distancias.put(inicio, 0);
        fila.offer(inicio);

        while (!fila.isEmpty()) {
            String atual = fila.poll();

            List<Rota> rotas = grafo.obterRotasPartindoDe(atual);
            if (rotas == null) continue;

            for (Rota rota : rotas) {
                String vizinho = rota.getDestino();
                if (distancias.get(vizinho) == Integer.MAX_VALUE) {
                    distancias.put(vizinho, distancias.get(atual) + 1);
                    fila.offer(vizinho);
                }
            }
        }
        return distancias;
    }

    public List<String> dijkstra(IGrafo grafo, String inicio, String alvo) {
        Map<String, Integer> distancias = new HashMap<>();
        Map<String, String> pai = new HashMap<>();
        PriorityQueue<Rota> filaPrioridade = new PriorityQueue<>(Comparator.comparingInt(Rota::getPeso));

        for (String cidade : grafo.obterCidades()) {
            distancias.put(cidade, Integer.MAX_VALUE);
        }

        distancias.put(inicio, 0);
        filaPrioridade.add(new Rota(inicio, inicio, 0));

        while (!filaPrioridade.isEmpty()) {
            Rota rotaAtual = filaPrioridade.poll();
            String atual = rotaAtual.getDestino();
            int distanciaAtual = rotaAtual.getPeso();

            if (distanciaAtual > distancias.get(atual)) {
                continue;
            }

            if (atual.equals(alvo)) {
                break;
            }

            List<Rota> rotas = grafo.obterRotasPartindoDe(atual);
            if (rotas == null) continue;

            for (Rota rotaVizinho : rotas) {
                String vizinho = rotaVizinho.getDestino();
                int pesoRota = rotaVizinho.getPeso();
                int novaDistancia = distancias.get(atual) + pesoRota;

                if (novaDistancia < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDistancia);
                    pai.put(vizinho, atual);
                    filaPrioridade.add(new Rota(atual, vizinho, novaDistancia));
                }
            }
        }

        if (!alvo.equals(inicio) && distancias.get(alvo) == Integer.MAX_VALUE) {
            return Collections.emptyList();
        }

        return reconstruirCaminho(pai, inicio, alvo);
    }

    public List<String> ordenacaoTopologica(IGrafo grafo) {
        Map<String, Integer> grauEntrada = new HashMap<>();
        Queue<String> fila = new LinkedList<>();
        List<String> resultado = new ArrayList<>();
        List<String> cidades = grafo.obterCidades();

        for (String nome : cidades) {
            grauEntrada.put(nome, 0);
        }

        Map<String, List<Rota>> adjacencias = grafo.obterTodasAdjacencias();

        for (List<Rota> rotas : adjacencias.values()) {
            for (Rota rota : rotas) {
                grauEntrada.computeIfPresent(rota.getDestino(), (k, v) -> v + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : grauEntrada.entrySet()) {
            if (entry.getValue() == 0) {
                fila.add(entry.getKey());
            }
        }

        while (!fila.isEmpty()) {
            String atual = fila.poll();
            resultado.add(atual);

            List<Rota> rotas = grafo.obterRotasPartindoDe(atual);
            if (rotas == null) continue;

            for (Rota rota : rotas) {
                String vizinho = rota.getDestino();
                grauEntrada.computeIfPresent(vizinho, (k, v) -> v - 1);

                if (grauEntrada.get(vizinho) == 0) {
                    fila.add(vizinho);
                }
            }
        }

        if (resultado.size() != cidades.size()) {
            System.out.println("ERRO: O Grafo contém um ciclo (não é um DAG).");
            return Collections.emptyList();
        }

        return resultado;
    }

    public List<Rota> arvoreGeradoraMinimaAGM(IGrafo grafo, String inicio) {
        List<Rota> agm = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        PriorityQueue<Rota> filaPrioridade = new PriorityQueue<>(Comparator.comparingInt(Rota::getPeso));

        visitados.add(inicio);

        List<Rota> rotasIniciais = grafo.obterRotasPartindoDe(inicio);
        if (rotasIniciais != null) {
            filaPrioridade.addAll(rotasIniciais);
        }

        while (!filaPrioridade.isEmpty()) {
            Rota rotaMenorCusto = filaPrioridade.poll();
            String destino = rotaMenorCusto.getDestino();

            if (!visitados.contains(destino)) {
                visitados.add(destino);
                agm.add(rotaMenorCusto);

                List<Rota> novasRotas = grafo.obterRotasPartindoDe(destino);
                if (novasRotas != null) {
                    for (Rota novaRota : novasRotas) {
                        if (!visitados.contains(novaRota.getDestino())) {
                            filaPrioridade.add(novaRota);
                        }
                    }
                }
            }
        }

        if (visitados.size() != grafo.obterCidades().size()) {
            System.out.println("AVISO: O grafo não é conexo. A AGM não inclui todas as cidades.");
        }

        return agm;
    }

    private List<String> reconstruirCaminho(Map<String, String> pai, String inicio, String alvo) {
        List<String> caminho = new LinkedList<>();
        String atual = alvo;

        if (!pai.containsKey(alvo) && !alvo.equals(inicio)) {
            return Collections.emptyList();
        }

        while (atual != null) {
            caminho.add(0, atual);
            if (atual.equals(inicio)) break;
            atual = pai.get(atual);
        }
        return caminho;
    }
}
