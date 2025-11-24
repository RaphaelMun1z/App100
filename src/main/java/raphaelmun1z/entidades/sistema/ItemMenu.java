package raphaelmun1z.entidades.sistema;

public class ItemMenu {
    String descricao;
    Runnable acao;

    public ItemMenu(String descricao, Runnable acao) {
        this.descricao = descricao;
        this.acao = acao;
    }
}