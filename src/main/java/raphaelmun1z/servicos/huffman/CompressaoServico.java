package raphaelmun1z.servicos.huffman;

import raphaelmun1z.dto.HuffmanNode;
import raphaelmun1z.dto.ResultadoCompressao;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class CompressaoServico {
    public ResultadoCompressao comprimir(String texto) {
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException("O texto não pode ser nulo ou vazio.");
        }

        Map<Character, Integer> freqMap = construirMapaFrequencia(texto);

        HuffmanNode arvore = construirArvoreHuffman(freqMap);

        Map<Character, String> mapaCodigos = construirMapaCodigos(arvore);

        String dadosComprimidos = codificarTexto(texto, mapaCodigos);

        return new ResultadoCompressao(dadosComprimidos, arvore);
    }

    public String descomprimir(ResultadoCompressao resultado) {
        if (resultado == null || resultado.dadosComprimidos() == null || resultado.arvore() == null) {
            throw new IllegalArgumentException("Resultado de compressão inválido.");
        }
        return decodificarTexto(resultado.dadosComprimidos(), resultado.arvore());
    }

    private Map<Character, Integer> construirMapaFrequencia(String texto) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char caractere : texto.toCharArray()) {
            freqMap.put(caractere, freqMap.getOrDefault(caractere, 0) + 1);
        }
        return freqMap;
    }

    private HuffmanNode construirArvoreHuffman(Map<Character, Integer> freqMap) {
        PriorityQueue<HuffmanNode> filaPrioridade = new PriorityQueue<>();

        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            filaPrioridade.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (filaPrioridade.size() > 1) {
            HuffmanNode noEsquerdo = filaPrioridade.poll();
            HuffmanNode noDireito = filaPrioridade.poll();
            filaPrioridade.add(new HuffmanNode(noEsquerdo, noDireito));
        }

        return filaPrioridade.poll();
    }

    private Map<Character, String> construirMapaCodigos(HuffmanNode arvore) {
        Map<Character, String> mapaCodigos = new HashMap<>();
        gerarCodigosRecursivo(arvore, "", mapaCodigos);
        return mapaCodigos;
    }

    private void gerarCodigosRecursivo(HuffmanNode no, String codigo, Map<Character, String> mapaCodigos) {
        if (no == null) {
            return;
        }

        if (no.isFolha()) {
            mapaCodigos.put(no.caractere(), codigo);
            return;
        }

        gerarCodigosRecursivo(no.noEsquerdo(), codigo + "0", mapaCodigos);
        gerarCodigosRecursivo(no.noDireito(), codigo + "1", mapaCodigos);
    }

    private String codificarTexto(String texto, Map<Character, String> mapaCodigos) {
        StringBuilder sb = new StringBuilder();
        for (char caractere : texto.toCharArray()) {
            sb.append(mapaCodigos.get(caractere));
        }
        return sb.toString();
    }

    private String decodificarTexto(String dadosComprimidos, HuffmanNode arvore) {
        StringBuilder sb = new StringBuilder();
        HuffmanNode noAtual = arvore;

        for (char bit : dadosComprimidos.toCharArray()) {
            if (bit == '0') {
                noAtual = noAtual.noEsquerdo();
            } else {
                noAtual = noAtual.noDireito();
            }

            if (noAtual.isFolha()) {
                sb.append(noAtual.caractere());
                noAtual = arvore;
            }
        }
        return sb.toString();
    }
}
