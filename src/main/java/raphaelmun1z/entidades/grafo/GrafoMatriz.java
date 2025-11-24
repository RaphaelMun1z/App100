package raphaelmun1z.entidades.grafo;

import raphaelmun1z.entidades.interfaces.IGrafo;

import java.util.ArrayList;
import java.util.List;

public class GrafoMatriz implements IGrafo {
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

    private int getIndice(String nome) {
        return cidades.indexOf(nome);
    }

    @Override
    public void adicionarCidade(String nome) {
        if (cidades.size() < capacidadeMaxima) {
            cidades.add(nome);
            System.out.println("Cidade adicionada: " + nome);
        } else {
            System.out.println("Capacidade máxima do mapa atingida!");
        }
    }

    @Override
    public void removerCidade(String nome) {
        int index = getIndice(nome);
        if (index != -1) {
            cidades.remove(index);
            for (int ii = 0; ii < capacidadeMaxima; ii++) {
                matriz[index][ii] = 0;
                matriz[ii][index] = 0;
            }
            System.out.println("Cidade removida (logicamente): " + nome);
        }
    }

    @Override
    public void adicionarRota(String origem, String destino, int peso, boolean bidirecional) {
        int ii = getIndice(origem);
        int jj = getIndice(destino);

        if (ii != -1 && jj != -1) {
            matriz[ii][jj] = peso;
            if (bidirecional) {
                matriz[jj][ii] = peso;
            }
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
        return (ii != -1 && jj != -1 && matriz[ii][jj] != 0);
    }

    @Override
    public void imprimirMapa() {
        System.out.println("\n--- Mapa da Baixada (Matriz) ---");
        System.out.print("      ");
        for (String c : cidades) System.out.printf("%10s", c);
        System.out.println();

        for (int ii = 0; ii < cidades.size(); ii++) {
            System.out.printf("%-10s", cidades.get(ii));
            for (int jj = 0; jj < cidades.size(); jj++) {
                System.out.printf("%10d", matriz[ii][jj]);
            }
            System.out.println();
        }
    }
}
