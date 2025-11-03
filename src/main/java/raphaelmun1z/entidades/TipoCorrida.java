package raphaelmun1z.entidades;

public class TipoCorrida {
    private Long id;
    private String categoria;
    private String descricao;

    public TipoCorrida(Long id, String categoria, String descricao) {
        this.id = id;
        this.categoria = categoria;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "#" + this.getId() +
            "\n Categoria: " + this.getCategoria() +
            "\n Descrição: " + this.getDescricao();
    }
}
