package raphaelmun1z.servicos.grafo;

import raphaelmun1z.entidades.grafo.GrafoMatriz;
import raphaelmun1z.entidades.grafo.Rota;
import raphaelmun1z.entidades.interfaces.IGrafo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Implementação de Grafo utilizando Matriz de Adjacência.
// Ao contrário da Lista, a Matriz aloca um espaço fixo na memória (capacidade x capacidade).
// É excelente para consultas rápidas de rotas diretas, mas tem um limite máximo de cidades suportadas.
public class GrafoMatrizService implements IGrafo {

    private final GrafoMatriz grafo;

    public GrafoMatrizService(int capacidade) {
        this.grafo = new GrafoMatriz(capacidade);
    }

    // Traduz o nome da cidade (String) para a coordenada numérica (índice) na matriz
    private int getIndice(String nome) {
        return grafo.getCidades().indexOf(nome);
    }

    @Override
    public void adicionarCidade(String nome) {
        // Como a matriz tem tamanho fixo, precisamos barrar a entrada
        // caso o limite físico (capacidadeMaxima) já tenha sido atingido.
        if (grafo.getCidades().size() < grafo.getCapacidadeMaxima()) {
            grafo.getCidades().add(nome);
            IO.println("Cidade adicionada: " + nome);
        } else {
            IO.println("Erro: Capacidade máxima do mapa atingida!");
        }
    }

    @Override
    public void removerCidade(String nome) {
        int index = getIndice(nome);
        if (index != -1) {
            grafo.getCidades().remove(index);

            int[][] matriz = grafo.getMatriz();
            int tamanhoAtual = grafo.getCidades().size();

            // REORGANIZAÇÃO DA MATRIZ (SHIFT)
            // Como removemos um vértice, precisamos "tapar o buraco" na matriz arrastando
            // os dados para cima e para a esquerda.

            // Passo 1: Desloca as linhas de baixo para cima, sobrescrevendo a linha da cidade removida
            for (int ii = index; ii < tamanhoAtual; ii++) {
                for (int jj = 0; jj <= tamanhoAtual; jj++) {
                    matriz[ii][jj] = matriz[ii + 1][jj];
                }
            }

            // Passo 2: Desloca as colunas da direita para a esquerda, sobrescrevendo a coluna da cidade removida
            for (int ii = 0; ii <= tamanhoAtual; ii++) {
                for (int jj = index; jj < tamanhoAtual; jj++) {
                    matriz[ii][jj] = matriz[ii][jj + 1];
                }
            }

            // Passo 3: Limpeza do lixo de memória
            // Como arrastamos os dados, a última linha e coluna ficam com dados duplicados.
            // Aqui nós zeramos essas posições "fantasmas" no final da matriz.
            for (int ii = 0; ii <= tamanhoAtual; ii++) {
                matriz[tamanhoAtual][ii] = 0;
                matriz[ii][tamanhoAtual] = 0;
            }

            IO.println("Cidade removida: " + nome);
        } else {
            IO.println("Cidade não encontrada.");
        }
    }

    @Override
    public void adicionarRota(String origem, String destino, int peso, boolean bidirecional) {
        int ii = getIndice(origem);
        int jj = getIndice(destino);

        if (ii != -1 && jj != -1) {
            // Na Matriz de Adjacência, a intersecção [linha][coluna] guarda o custo da aresta.
            grafo.getMatriz()[ii][jj] = peso;

            // Se for mão dupla, espelhamos a intersecção [coluna][linha]
            if (bidirecional) {
                grafo.getMatriz()[jj][ii] = peso;
            }
        } else {
            IO.println("Erro: Cidades não encontradas para criar rota.");
        }
    }

    @Override
    public void removerRota(String origem, String destino, boolean bidirecional) {
        // Aproveita a lógica de adição, mas injeta o peso '0'.
        // O valor 0 representa a ausência de conexão física entre as cidades nessa estrutura.
        adicionarRota(origem, destino, 0, bidirecional);
    }

    @Override
    public boolean temRota(String origem, String destino) {
        int ii = getIndice(origem);
        int jj = getIndice(destino);

        // Valida se as cidades existem no índice e verifica se a intersecção delas tem um peso válido
        return (ii != -1 && jj != -1 && grafo.getMatriz()[ii][jj] != 0);
    }

    @Override
    public void imprimirMapa() {
        IO.println("\n--- Mapa da Baixada (Matriz) ---");

        // Imprime o cabeçalho (nomes das cidades nas colunas)
        System.out.print("      ");
        for (String c : grafo.getCidades()) System.out.printf("%10s", c);
        IO.println();

        int tamanho = grafo.getCidades().size();
        int[][] m = grafo.getMatriz();

        // Varre a matriz formatando a saída para uma tabela visual.
        // Zeros são substituídos por "-" para facilitar a leitura das rotas inexistentes.
        for (int ii = 0; ii < tamanho; ii++) {
            System.out.printf("%-10s", grafo.getCidades().get(ii));
            for (int jj = 0; jj < tamanho; jj++) {
                int peso = m[ii][jj];
                String valor = (peso == 0) ? "-" : String.valueOf(peso);
                System.out.printf("%10s", valor);
            }
            IO.println();
        }
    }

    @Override
    public List<Rota> obterRotasPartindoDe(String origem) {
        List<Rota> rotasDeSaida = new ArrayList<>();
        int ii = getIndice(origem);

        if (ii != -1) {
            int tamanho = grafo.getCidades().size();
            int[][] matriz = grafo.getMatriz();

            // Lê a linha inteira da cidade de origem.
            // Qualquer coluna que não tenha peso 0 significa um destino válido.
            for (int jj = 0; jj < tamanho; jj++) {
                int peso = matriz[ii][jj];
                if (peso != 0) {
                    String destino = grafo.getCidades().get(jj);
                    rotasDeSaida.add(new Rota(origem, destino, peso));
                }
            }
        }
        return rotasDeSaida;
    }

    @Override
    public List<String> obterCidades() {
        return grafo.getCidades();
    }

    @Override
    public Map<String, List<Rota>> obterTodasAdjacencias() {
        Map<String, List<Rota>> todasAdjacencias = new HashMap<>();
        List<String> cidades = grafo.getCidades();

        for (String cidade : cidades) {
            todasAdjacencias.put(cidade, obterRotasPartindoDe(cidade));
        }
        return todasAdjacencias;
    }
}