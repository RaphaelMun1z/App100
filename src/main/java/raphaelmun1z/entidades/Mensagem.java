package raphaelmun1z.entidades;

public class Mensagem {
    private Long id;
    private Usuario origem;
    private Usuario destino;
    private String texto;

    public Mensagem(Long id, Usuario origem, Usuario destino, String texto) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.texto = texto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getOrigem() {
        return origem;
    }

    public void setOrigem(Usuario origem) {
        this.origem = origem;
    }

    public Usuario getDestino() {
        return destino;
    }

    public void setDestino(Usuario destino) {
        this.destino = destino;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return origem.getNome() + ": " + texto;
    }
}
