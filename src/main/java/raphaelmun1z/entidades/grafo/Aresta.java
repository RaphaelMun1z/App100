package raphaelmun1z.entidades.grafo;

public class Aresta {
    private String destino;
    private int peso;

    public Aresta(String destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public String getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return destino + "(" + peso + "km)";
    }
}