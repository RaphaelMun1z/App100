package raphaelmun1z.servicos.grafo;

import raphaelmun1z.entidades.grafo.GrafoMatriz;
import raphaelmun1z.entidades.grafo.Rota;
import raphaelmun1z.entidades.interfaces.IGrafo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoMatrizService implements IGrafo {

    private final GrafoMatriz grafo;

    public GrafoMatrizService(int capacidade) {
        this.grafo = new GrafoMatriz(capacidade);
    }

    private int getIndice(String nome) {
        return grafo.getCidades().indexOf(nome);
    }

    @Override
    public void adicionarCidade(String nome) {
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

            for (int ii = index; ii < tamanhoAtual; ii++) {
                for (int jj = 0; jj <= tamanhoAtual; jj++) {
                    matriz[ii][jj] = matriz[ii + 1][jj];
                }
            }

            for (int ii = 0; ii <= tamanhoAtual; ii++) {
                for (int jj = index; jj < tamanhoAtual; jj++) {
                    matriz[ii][jj] = matriz[ii][jj + 1];
                }
            }

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
            grafo.getMatriz()[ii][jj] = peso;
            if (bidirecional) {
                grafo.getMatriz()[jj][ii] = peso;
            }
        } else {
            IO.println("Erro: Cidades não encontradas para criar rota.");
        }
    }

    @Override
    public void removerRota(String origem, String destino, boolean bidirecional) {
        adicionarRota(origem, destino, 0, bidirecional);
    }

    @Override
    public boolean temRota(String origem, String destino) {
        int ii = getIndice(origem);
        int jj = getIndice(destino);
        return (ii != -1 && jj != -1 && grafo.getMatriz()[ii][jj] != 0);
    }

    @Override
    public void imprimirMapa() {
        IO.println("\n--- Mapa da Baixada (Matriz) ---");

        System.out.print("      ");
        for (String c : grafo.getCidades()) System.out.printf("%10s", c);
        IO.println();

        int tamanho = grafo.getCidades().size();
        int[][] m = grafo.getMatriz();

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