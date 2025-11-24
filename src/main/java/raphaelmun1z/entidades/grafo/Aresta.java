package raphaelmun1z.entidades.grafo;

public class Aresta {
    String destino;
    int peso;

    public Aresta(String destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    @Override
    public String toString() {
        return destino + "(" + peso + "km)";
    }
}