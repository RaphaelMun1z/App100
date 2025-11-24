package raphaelmun1z.entidades.huffman;

public class RelatorioCompressao {
    private String textoOriginal;
    private String textoComprimidoBinario;
    private String textoDescomprimido;
    private long tempoExecucaoNano;

    public RelatorioCompressao(String textoOriginal, String textoComprimidoBinario, String textoDescomprimido, long tempoExecucaoNano) {
        this.textoOriginal = textoOriginal;
        this.textoComprimidoBinario = textoComprimidoBinario;
        this.textoDescomprimido = textoDescomprimido;
        this.tempoExecucaoNano = tempoExecucaoNano;
    }

    public int getTamanhoOriginalBits() {
        return textoOriginal.length() * 16;
    }

    public int getTamanhoComprimidoBits() {
        return textoComprimidoBinario.length();
    }

    public double getPercentualReducao() {
        double original = getTamanhoOriginalBits();
        double comprimido = getTamanhoComprimidoBits();
        if (original == 0) return 0.0;
        return 100.0 * (original - comprimido) / original;
    }

    public boolean isIntegro() {
        return textoOriginal.equals(textoDescomprimido);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Texto Original: ").append(textoOriginal).append("\n");
        sb.append("Texto Comprimido (bin): ").append(textoComprimidoBinario).append("\n");
        sb.append("Texto Descomprimido: ").append(textoDescomprimido).append("\n");
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("Tamanho Original: %d bits\n", getTamanhoOriginalBits()));
        sb.append(String.format("Tamanho Comprimido: %d bits\n", getTamanhoComprimidoBits()));
        sb.append(String.format("Redução de Espaço: %.2f%%\n", getPercentualReducao()));
        sb.append("Tempo de Compressão: ").append(tempoExecucaoNano).append(" ns\n");
        sb.append("Integridade: ").append(isIntegro() ? "SUCESSO" : "FALHA");

        return sb.toString();
    }
}