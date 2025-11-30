package raphaelmun1z.entidades.grafo;

public class Rota {
    private String origem;
    private String destino;
    private int peso;

    public Rota(String origem, String destino, int peso) {
        this.origem = origem;
        this.destino = destino;
        this.peso = peso;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }
}