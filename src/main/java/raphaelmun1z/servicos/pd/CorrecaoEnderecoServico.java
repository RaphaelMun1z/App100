package raphaelmun1z.servicos.pd;

public class CorrecaoEnderecoServico {

    public int calcularDistancia(String s1, String s2) {
        int lenS1 = s1.length();
        int lenS2 = s2.length();

        int[][] matrizCustos = new int[lenS1 + 1][lenS2 + 1];

        for (int ii = 0; ii <= lenS1; ii++) {
            for (int jj = 0; jj <= lenS2; jj++) {
                if (ii == 0) {
                    matrizCustos[ii][jj] = jj;
                } else if (jj == 0) {
                    matrizCustos[ii][jj] = ii;
                } else if (s1.charAt(ii - 1) == s2.charAt(jj - 1)) {
                    matrizCustos[ii][jj] = matrizCustos[ii - 1][jj - 1];
                } else {
                    matrizCustos[ii][jj] = 1 + Math.min(matrizCustos[ii - 1][jj],
                            Math.min(matrizCustos[ii][jj - 1],
                                    matrizCustos[ii - 1][jj - 1]));
                }
            }
        }
        return matrizCustos[lenS1][lenS2];
    }

    public String sugerirCorrecao(String entrada, java.util.Set<String> cidadesExistentes) {
        String melhorSugestao = null;
        int menorDistancia = Integer.MAX_VALUE;

        for (String cidade : cidadesExistentes) {
            int distancia = calcularDistancia(entrada.toLowerCase(), cidade.toLowerCase());
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                melhorSugestao = cidade;
            }
        }

        return (menorDistancia <= 3) ? melhorSugestao : entrada;
    }
}