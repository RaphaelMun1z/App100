package raphaelmun1z.servicos.grafo;

import raphaelmun1z.entidades.grafo.Rota;
import raphaelmun1z.entidades.interfaces.IGrafo;

import java.util.*;

public class GrafoOtimizacaoService {

    // ==========================================
    // ALGORITMO: Busca em Profundidade (DFS - Depth-First Search)
    // OBJETIVO: Explorar um caminho até o fim (o mais profundo possível) antes de retroceder.
    // É ótimo para encontrar *qualquer* caminho rápido ou checar se um alvo é alcançável.
    // ESTRUTURA CHAVE: Pilha (Stack) - O último a entrar é o primeiro a ser explorado.
    // ==========================================
    //
    public List<String> buscaProfundidadeDFS(IGrafo grafo, String inicio, String alvo) {
        Set<String> visitados = new HashSet<>();
        Stack<String> pilha = new Stack<>();

        // Mapeia "de onde viemos" para podermos traçar a rota de volta no final
        Map<String, String> pai = new HashMap<>();

        pilha.push(inicio);
        visitados.add(inicio);

        while (!pilha.isEmpty()) {
            String atual = pilha.pop();

            // Se chegamos no destino, interrompemos a busca e montamos o caminho percorrido
            if (atual.equals(alvo)) {
                return reconstruirCaminho(pai, inicio, alvo);
            }

            List<Rota> rotas = grafo.obterRotasPartindoDe(atual);
            if (rotas == null) continue;

            // Empilha os vizinhos não visitados. Como é uma pilha, o último vizinho adicionado
            // será o próximo a ser explorado, forçando o algoritmo a ir "fundo" no mapa.
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

    // ==========================================
    // ALGORITMO: Busca em Largura (BFS - Breadth-First Search)
    // OBJETIVO: Explorar o mapa em "camadas". Encontra o caminho com o MENOR NÚMERO DE SALTOS (conexões),
    // independentemente do peso/distância da rota.
    // ESTRUTURA CHAVE: Fila (Queue) - O primeiro a entrar é o primeiro a ser explorado.
    // ==========================================
    //
    public Map<String, Integer> buscaLarguraBFS(IGrafo grafo, String inicio) {
        Map<String, Integer> distancias = new HashMap<>();
        Queue<String> fila = new LinkedList<>();

        // Inicializa todas as distâncias como "infinito", pois ainda não sabemos como chegar nelas
        for (String cidade : grafo.obterCidades()) {
            distancias.put(cidade, Integer.MAX_VALUE);
        }

        distancias.put(inicio, 0);
        fila.offer(inicio);

        while (!fila.isEmpty()) {
            String atual = fila.poll();

            List<Rota> rotas = grafo.obterRotasPartindoDe(atual);
            if (rotas == null) continue;

            // Enfileira os vizinhos. Isso garante que vamos processar todos os vizinhos diretos (distância 1)
            // antes de processar os vizinhos dos vizinhos (distância 2).
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

    // ==========================================
    // ALGORITMO: Dijkstra (Caminho Mínimo)
    // OBJETIVO: Encontrar a rota mais BARATA/CURTA considerando o "peso" das arestas (km, tempo, custo).
    // É a lógica base por trás de GPS como o Waze e Google Maps.
    // ESTRUTURA CHAVE: Fila de Prioridade (PriorityQueue) - Sempre pega a rota de menor custo primeiro.
    // ==========================================
    //
    public List<String> dijkstra(IGrafo grafo, String inicio, String alvo) {
        Map<String, Integer> distancias = new HashMap<>();
        Map<String, String> pai = new HashMap<>();

        // Fila que se auto-ordena baseada no menor peso da rota
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

            // Ignora rotas obsoletas se já encontramos um caminho melhor para este nó
            if (distanciaAtual > distancias.get(atual)) {
                continue;
            }

            if (atual.equals(alvo)) {
                break; // Chegamos no destino com o menor custo possível
            }

            List<Rota> rotas = grafo.obterRotasPartindoDe(atual);
            if (rotas == null) continue;

            // Relaxamento: Verifica se o caminho passando pelo nó 'atual' é mais barato
            // do que o caminho que conhecíamos anteriormente para o 'vizinho'.
            for (Rota rotaVizinho : rotas) {
                String vizinho = rotaVizinho.getDestino();
                int pesoRota = rotaVizinho.getPeso();
                int novaDistancia = distancias.get(atual) + pesoRota;

                // Se for mais barato, atualiza a melhor rota e avisa a fila de prioridade
                if (novaDistancia < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDistancia);
                    pai.put(vizinho, atual);
                    filaPrioridade.add(new Rota(atual, vizinho, novaDistancia));
                }
            }
        }

        if (!alvo.equals(inicio) && distancias.get(alvo) == Integer.MAX_VALUE) {
            return Collections.emptyList(); // Destino inalcançável
        }

        return reconstruirCaminho(pai, inicio, alvo);
    }

    // ==========================================
    // ALGORITMO: Ordenação Topológica (Algoritmo de Kahn)
    // OBJETIVO: Organizar nós que têm dependências entre si. Ex: "A cidade B só pode ser visitada
    // depois da cidade A" (ou pré-requisitos de disciplinas na faculdade).
    // ESTRUTURA CHAVE: Fila e cálculo de "Grau de Entrada" (quantas setas apontam para o nó).
    // ==========================================
    //
    public List<String> ordenacaoTopologica(IGrafo grafo) {
        Map<String, Integer> grauEntrada = new HashMap<>();
        Queue<String> fila = new LinkedList<>();
        List<String> resultado = new ArrayList<>();
        List<String> cidades = grafo.obterCidades();

        // Inicializa todas as cidades com 0 dependências
        for (String nome : cidades) {
            grauEntrada.put(nome, 0);
        }

        // Calcula quantas arestas entram em cada cidade
        Map<String, List<Rota>> adjacencias = grafo.obterTodasAdjacencias();
        for (List<Rota> rotas : adjacencias.values()) {
            for (Rota rota : rotas) {
                grauEntrada.computeIfPresent(rota.getDestino(), (k, v) -> v + 1);
            }
        }

        // Só podemos iniciar a ordem pelas cidades que não dependem de ninguém (grau de entrada == 0)
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

            // Ao processar uma cidade, removemos suas "setas" de saída, diminuindo a
            // dependência de seus vizinhos.
            for (Rota rota : rotas) {
                String vizinho = rota.getDestino();
                grauEntrada.computeIfPresent(vizinho, (k, v) -> v - 1);

                // Se o vizinho não tem mais dependências pendentes, ele entra na fila para ser processado
                if (grauEntrada.get(vizinho) == 0) {
                    fila.add(vizinho);
                }
            }
        }

        // Se o resultado não tem o tamanho total do grafo, significa que há uma dependência circular
        // (um ciclo), o que quebra a lógica da ordenação topológica.
        if (resultado.size() != cidades.size()) {
            System.out.println("ERRO: O Grafo contém um ciclo (não é um DAG).");
            return Collections.emptyList();
        }

        return resultado;
    }

    // ==========================================
    // ALGORITMO: Árvore Geradora Mínima (AGM) - Algoritmo de Prim
    // OBJETIVO: Conectar TODOS os nós do grafo usando o MENOR custo total de arestas,
    // sem formar ciclos. Ideal para projetar redes de estradas, fibra ótica, canos, etc.
    // ESTRUTURA CHAVE: Fila de Prioridade (PriorityQueue) para expandir a "fronteira" da árvore pela rota mais barata.
    // ==========================================
    //
    public List<Rota> arvoreGeradoraMinimaAGM(IGrafo grafo, String inicio) {
        List<Rota> agm = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        PriorityQueue<Rota> filaPrioridade = new PriorityQueue<>(Comparator.comparingInt(Rota::getPeso));

        visitados.add(inicio);

        // Adiciona à fila de prioridade todas as fronteiras saindo do ponto de partida
        List<Rota> rotasIniciais = grafo.obterRotasPartindoDe(inicio);
        if (rotasIniciais != null) {
            filaPrioridade.addAll(rotasIniciais);
        }

        // Enquanto a fronteira tiver rotas para expandir
        while (!filaPrioridade.isEmpty()) {
            Rota rotaMenorCusto = filaPrioridade.poll();
            String destino = rotaMenorCusto.getDestino();

            // Só expandimos a árvore para cidades que ainda não fazem parte dela (evita ciclos)
            if (!visitados.contains(destino)) {
                visitados.add(destino);
                agm.add(rotaMenorCusto);

                // Adiciona as novas rotas disponíveis a partir desta nova cidade à fronteira
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

    // ==========================================
    // FUNÇÃO AUXILIAR: Reconstruir Caminho
    // OBJETIVO: Lê o mapa de "quem descobriu quem" de trás para frente (do alvo para o início)
    // e devolve a lista na ordem correta da viagem. Usado pelo DFS e Dijkstra.
    // ==========================================
    private List<String> reconstruirCaminho(Map<String, String> pai, String inicio, String alvo) {
        List<String> caminho = new LinkedList<>();
        String atual = alvo;

        if (!pai.containsKey(alvo) && !alvo.equals(inicio)) {
            return Collections.emptyList();
        }

        // Volta seguindo os "pais" e adiciona sempre no índice 0 para inverter a ordem naturalmente
        while (atual != null) {
            caminho.add(0, atual);
            if (atual.equals(inicio)) break;
            atual = pai.get(atual); // Pula para o nó anterior da rota
        }
        return caminho;
    }
}