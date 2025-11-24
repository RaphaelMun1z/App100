package raphaelmun1z.entidades.grafo;

import java.util.ArrayList;
import java.util.List;

public class GrafoMatriz {
    private int[][] matriz;
    private List<String> cidades;
    private int capacidadeMaxima;

    public GrafoMatriz(int capacidade) {
        this.capacidadeMaxima = capacidade;
        this.matriz = new int[capacidade][capacidade];
        this.cidades = new ArrayList<>();

        for (int ii = 0; ii < capacidade; ii++) {
            for (int jj = 0; jj < capacidade; jj++) {
                matriz[ii][jj] = 0;
            }
        }
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public List<String> getCidades() {
        return cidades;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }
}
