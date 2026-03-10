package raphaelmun1z.servicos.pd;

public class CorrecaoEnderecoServico {

    // ==========================================
    // ALGORITMO: Distância de Levenshtein (Programação Dinâmica)
    // OBJETIVO: Calcular o número mínimo de operações (inserir, deletar ou substituir um caractere)
    // necessárias para transformar a string s1 na string s2. Usado para "fuzzy search" (busca aproximada).
    // ==========================================
    public int calcularDistancia(String s1, String s2) {
        int lenS1 = s1.length();
        int lenS2 = s2.length();

        // Matriz de memoização (Programação Dinâmica).
        // Armazena o custo das transformações parciais para não precisarmos recalcular
        // pedaços da palavra que já foram analisados.
        int[][] matrizCustos = new int[lenS1 + 1][lenS2 + 1];

        for (int ii = 0; ii <= lenS1; ii++) {
            for (int jj = 0; jj <= lenS2; jj++) {

                // Casos base: se uma das palavras for vazia, o custo é o tamanho da outra
                // (ou seja, teríamos que inserir todas as letras da outra palavra).
                if (ii == 0) {
                    matrizCustos[ii][jj] = jj;
                } else if (jj == 0) {
                    matrizCustos[ii][jj] = ii;

                    // Se os caracteres atuais forem iguais, nenhuma operação é necessária.
                    // Herdamos o custo do subproblema anterior (diagonal da matriz).
                } else if (s1.charAt(ii - 1) == s2.charAt(jj - 1)) {
                    matrizCustos[ii][jj] = matrizCustos[ii - 1][jj - 1];

                    // Se os caracteres forem diferentes, testamos 3 cenários e pegamos o de MENOR custo:
                    // 1. matrizCustos[ii - 1][jj] -> Custo de deletar
                    // 2. matrizCustos[ii][jj - 1] -> Custo de inserir
                    // 3. matrizCustos[ii - 1][jj - 1] -> Custo de substituir
                    // Somamos 1 ao final, que representa o "gasto" da operação escolhida agora.
                } else {
                    matrizCustos[ii][jj] = 1 + Math.min(matrizCustos[ii - 1][jj],
                            Math.min(matrizCustos[ii][jj - 1],
                                    matrizCustos[ii - 1][jj - 1]));
                }
            }
        }

        // O resultado final (menor número de edições) fica na última célula da matriz
        return matrizCustos[lenS1][lenS2];
    }

    // ==========================================
    // INTEGRAÇÃO COM A REGRA DE NEGÓCIO
    // OBJETIVO: Pegar o input errado do usuário e cruzar com o banco de cidades válidas
    // para sugerir o nome correto.
    // ==========================================
    public String sugerirCorrecao(String entrada, java.util.Set<String> cidadesExistentes) {
        String melhorSugestao = null;
        int menorDistancia = Integer.MAX_VALUE;

        // Varre o dicionário de cidades cadastradas avaliando qual tem a menor
        // taxa de divergência (distância) em relação ao que o usuário digitou.
        for (String cidade : cidadesExistentes) {
            int distancia = calcularDistancia(entrada.toLowerCase(), cidade.toLowerCase());
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                melhorSugestao = cidade;
            }
        }

        // Limiar de tolerância (Threshold): Só aceitamos a sugestão se a diferença for
        // de no máximo 3 erros de digitação. Se for maior que isso, assumimos que o
        // usuário realmente digitou algo novo ou fora de contexto, e mantemos a entrada original.
        return (menorDistancia <= 3) ? melhorSugestao : entrada;
    }
}