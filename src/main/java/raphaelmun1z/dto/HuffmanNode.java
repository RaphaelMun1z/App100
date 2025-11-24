package raphaelmun1z.dto;

public record HuffmanNode(
    Character caractere,
    Integer frequencia,
    HuffmanNode noEsquerdo,
    HuffmanNode noDireito
) implements Comparable<HuffmanNode> {
    public HuffmanNode(Character caractere, Integer frequencia) {
        this(caractere, frequencia, null, null);
    }

    public HuffmanNode(HuffmanNode noEsquerdo, HuffmanNode noDireito) {
        this(null, noEsquerdo.frequencia() + noDireito.frequencia(), noEsquerdo, noDireito);
    }

    @Override
    public int compareTo(HuffmanNode outro) {
        return this.frequencia - outro.frequencia;
    }

    public boolean isFolha() {
        return this.caractere != null;
    }
}