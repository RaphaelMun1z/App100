package raphaelmun1z.servicos.huffman;

import raphaelmun1z.dto.HuffmanNode;
import raphaelmun1z.dto.ResultadoCompressao;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class CompressaoServico {

    // ==========================================
    // ALGORITMO: Compressão de Huffman
    // OBJETIVO: Reduzir o tamanho de um texto baseando-se na frequência de seus caracteres.
    // Caracteres que aparecem muito ganham códigos binários curtos, os raros ganham códigos longos.
    // ==========================================
    public ResultadoCompressao comprimir(String texto) {
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException("O texto não pode ser nulo ou vazio.");
        }

        // Fluxo principal da compressão:
        // 1. Descobrir a frequência de cada letra
        Map<Character, Integer> freqMap = construirMapaFrequencia(texto);

        // 2. Montar a Árvore de Huffman com base nas frequências
        HuffmanNode arvore = construirArvoreHuffman(freqMap);

        // 3. Extrair o "dicionário" de conversão (ex: 'A' -> "01", 'B' -> "100")
        Map<Character, String> mapaCodigos = construirMapaCodigos(arvore);

        // 4. Substituir o texto original pela sequência de bits comprimida
        String dadosComprimidos = codificarTexto(texto, mapaCodigos);

        // O resultado precisa devolver a árvore junto, senão será impossível descomprimir depois
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

        // Contabiliza a ocorrência de cada caractere.
        // Essa é a base matemática para a compressão funcionar.
        for (char caractere : texto.toCharArray()) {
            freqMap.put(caractere, freqMap.getOrDefault(caractere, 0) + 1);
        }
        return freqMap;
    }

    private HuffmanNode construirArvoreHuffman(Map<Character, Integer> freqMap) {
        // A Fila de Prioridade é crucial aqui: ela se auto-organiza para deixar
        // os nós com MENOR frequência sempre no topo (prontos para serem removidos primeiro).
        PriorityQueue<HuffmanNode> filaPrioridade = new PriorityQueue<>();

        // Transforma o mapa de frequências em nós isolados
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            filaPrioridade.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // Construção da árvore "bottom-up" (de baixo para cima):
        // Pega sempre os dois nós menos frequentes, junta os dois debaixo de um novo nó pai,
        // e devolve o pai para a fila. Repete até sobrar só um nó (a raiz da árvore).
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

    // Navegação DFS (Busca em Profundidade) para desenhar os caminhos da árvore.
    // Regra universal do Huffman: ir para a esquerda = bit 0, ir para a direita = bit 1.
    private void gerarCodigosRecursivo(HuffmanNode no, String codigo, Map<Character, String> mapaCodigos) {
        if (no == null) {
            return;
        }

        // Quando chega em uma folha (nó sem filhos), significa que encontramos um caractere.
        // O "caminho" (0s e 1s) percorrido até aqui se torna o código oficial dessa letra.
        if (no.isFolha()) {
            mapaCodigos.put(no.caractere(), codigo);
            return;
        }

        gerarCodigosRecursivo(no.noEsquerdo(), codigo + "0", mapaCodigos);
        gerarCodigosRecursivo(no.noDireito(), codigo + "1", mapaCodigos);
    }

    private String codificarTexto(String texto, Map<Character, String> mapaCodigos) {
        StringBuilder sb = new StringBuilder();

        // Substituição simples usando o dicionário gerado na etapa anterior
        for (char caractere : texto.toCharArray()) {
            sb.append(mapaCodigos.get(caractere));
        }
        return sb.toString();
    }

    private String decodificarTexto(String dadosComprimidos, HuffmanNode arvore) {
        StringBuilder sb = new StringBuilder();
        HuffmanNode noAtual = arvore;

        // Para descomprimir, usamos os bits como "GPS" dentro da árvore de Huffman
        for (char bit : dadosComprimidos.toCharArray()) {
            if (bit == '0') {
                noAtual = noAtual.noEsquerdo();
            } else {
                noAtual = noAtual.noDireito();
            }

            // Se o caminho nos levou a uma folha, achamos o caractere original.
            // Registramos a letra e reiniciamos o ponteiro de volta para a raiz da árvore
            // para decodificar a próxima sequência de bits.
            if (noAtual.isFolha()) {
                sb.append(noAtual.caractere());
                noAtual = arvore;
            }
        }
        return sb.toString();
    }
}