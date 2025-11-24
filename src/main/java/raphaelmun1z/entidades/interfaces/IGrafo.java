package raphaelmun1z.entidades.interfaces;

public interface IGrafo {
    void adicionarCidade(String nome);

    void removerCidade(String nome);

    void adicionarRota(String origem, String destino, int peso, boolean bidirecional);

    void removerRota(String origem, String destino, boolean bidirecional);

    boolean temRota(String origem, String destino);

    void imprimirMapa();
}
