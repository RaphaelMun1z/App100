package raphaelmun1z.entidades.interfaces;

import raphaelmun1z.entidades.grafo.Rota;

import java.util.List;
import java.util.Map;

public interface IGrafo {
    void adicionarCidade(String nome);

    void removerCidade(String nome);

    void adicionarRota(String origem, String destino, int peso, boolean bidirecional);

    void removerRota(String origem, String destino, boolean bidirecional);

    boolean temRota(String origem, String destino);

    void imprimirMapa();

    List<Rota> obterRotasPartindoDe(String origem);

    List<String> obterCidades();

    Map<String, List<Rota>> obterTodasAdjacencias();
}
