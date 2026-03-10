package raphaelmun1z.servicos.rabinKarp;

import raphaelmun1z.entidades.rabinKarp.Chat;
import raphaelmun1z.entidades.rabinKarp.Mensagem;
import raphaelmun1z.entidades.usuario.Motorista;
import raphaelmun1z.entidades.usuario.Passageiro;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatServico {
    private List<Chat> historicoChats = new ArrayList<>();

    // Dicionário de palavras proibidas na plataforma que vão disparar o filtro
    private final List<String> PADROES_SENSIVEIS = Arrays.asList(
            "fraude", "ameaça", "golpe", "ofensa"
    );

    // Variáveis matemáticas essenciais para o cálculo do Hash no algoritmo de Rabin-Karp
    // Q é um número primo aleatório usado para aplicar a operação de módulo (evita estouro de limite de memória no cálculo).
    private final long Q = 101;

    // D é a base do nosso sistema numérico (256 porque estamos lidando com a tabela ASCII estendida).
    private final int D = 256;

    public void mockDados() {
        historicoChats.add(new Chat(1L, new Motorista(1L, "João", "Silva", LocalDate.now(), 0), new Passageiro(1L, "Letícia", "Lopes", LocalDate.now(), 0)));
    }

    public void imprimeMensagens(Chat chat) {
        if (chat.getMensagens().isEmpty()) {
            IO.println("Não há mensagens.");
            return;
        }

        System.out.println("--- Histórico do Chat ---");
        chat.getMensagens().forEach(IO::println);
    }

    // Ponto de interceptação da regra de negócio:
    // Antes de efetivamente salvar a mensagem no chat, varremos o texto em busca de padrões.
    public void enviarMensagem(Chat chat, Mensagem msg){
        if(analisaMensagemViaRabinKarp(msg.getTexto())){
            msg.setTexto("Mensagem Censurada!");
        }
        chat.addMensagem(msg);
    }

    // Varre a mensagem cruzando com todas as palavras do nosso dicionário de proibições
    public Boolean analisaMensagemViaRabinKarp(String msg) {
        for (String padrao : PADROES_SENSIVEIS) {
            if (buscaPadrao(msg, padrao)) {
                return true;
            }
        }
        return false;
    }

    // ==========================================
    // ALGORITMO: Busca de Padrões (String Matching) com Rabin-Karp
    // OBJETIVO: Encontrar uma substring (palavra proibida) dentro de um texto maior.
    // O pulo do gato aqui é usar "Rolling Hash": em vez de comparar letra por letra,
    // transformamos as palavras em números (hashes) e comparamos apenas os números.
    // ==========================================
    private boolean buscaPadrao(String texto, String padrao) {
        int M = padrao.length();
        int N = texto.length();

        if (M == 0 || N < M) {
            return false;
        }

        long hashPadrao = 0;
        long hashTexto = 0;
        long h = 1;

        // Pré-calcula o valor do dígito mais significativo ('h').
        // Ele será usado lá na frente para remover o valor da primeira letra
        // quando formos "deslizar" a janela de leitura pela mensagem.
        for (int ii = 0; ii < M - 1; ii++) {
            h = (h * D) % Q;
        }

        // Calcula o hash inicial da palavra que estamos procurando (hashPadrao)
        // e o hash do primeiro pedaço do texto (hashTexto) do exato mesmo tamanho da palavra.
        for (int ii = 0; ii < M; ii++) {
            hashPadrao = (D * hashPadrao + padrao.charAt(ii)) % Q;
            hashTexto = (D * hashTexto + texto.charAt(ii)) % Q;
        }

        // "Desliza" a janela de leitura ao longo do texto inteiro
        for (int ii = 0; ii <= N - M; ii++) {

            // Se os números (hashes) baterem, temos um possível match.
            if (hashPadrao == hashTexto) {
                // Como pode haver "Colisão de Hash" (duas palavras diferentes gerarem o mesmo número),
                // fazemos a verificação exata letra por letra só para ter certeza de que achamos a palavra certa.
                if (padrao.equals(texto.substring(ii, ii + M))) {
                    return true;
                }
            }

            // A MÁGICA DO ROLLING HASH ACONTECE AQUI:
            // Se não bateu e ainda temos texto para ler, recalculamos o hash do texto para o próximo passo.
            // Em vez de calcular do zero, apenas:
            // 1. Subtraímos o valor matemático da letra que ficou para trás (saiu da janela).
            // 2. Multiplicamos pela base e somamos o valor da nova letra que entrou na janela.
            if (ii < N - M) {
                hashTexto = (D * (hashTexto - texto.charAt(ii) * h));
                hashTexto = (hashTexto + texto.charAt(ii + M)) % Q;

                // Em linguagens como Java, o módulo pode retornar negativo, então ajustamos para positivo
                if (hashTexto < 0) {
                    hashTexto = (hashTexto + Q);
                }
            }
        }
        return false;
    }
}