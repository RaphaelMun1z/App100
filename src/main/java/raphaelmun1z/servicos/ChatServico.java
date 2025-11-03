package raphaelmun1z.servicos;

import raphaelmun1z.entidades.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatServico {
    private List<Chat> historicoChats = new ArrayList<>();

    private final List<String> PADROES_SENSIVEIS = Arrays.asList(
        "fraude", "ameaça", "golpe", "ofensa"
    );

    private final long Q = 101;
    private final int D = 256;

    public void mockDados() {
        historicoChats.add(new Chat(1L, new Motorista(1L, "João", "Silva", LocalDate.now(), 0), new Passageiro(1L, "Letícia", "Lopes", LocalDate.now(), 0)));
    }

    public void imprimeMensagens(Chat chat) {
        if (chat.getMensagens().isEmpty()) {
            IO.println("Não há mensagens.");
            return;
        }

        IO.println(this);
        chat.getMensagens().forEach(IO::println);
    }

    public void enviarMensagem(Chat chat, Mensagem msg){
        if(analisaMensagemViaRabinKarp(msg.getTexto())){
            msg.setTexto("Mensagem Censurada!");
        }
        chat.addMensagem(msg);
    }

    public Boolean analisaMensagemViaRabinKarp(String msg) {
        for (String padrao : PADROES_SENSIVEIS) {
            if (buscaPadrao(msg, padrao)) {
                return true;
            }
        }
        return false;
    }

    private boolean buscaPadrao(String texto, String padrao) {
        int M = padrao.length();
        int N = texto.length();

        if (M == 0 || N < M) {
            return false;
        }

        long hashPadrao = 0;
        long hashTexto = 0;
        long h = 1;

        for (int ii = 0; ii < M - 1; ii++) {
            h = (h * D) % Q;
        }

        for (int ii = 0; ii < M; ii++) {
            hashPadrao = (D * hashPadrao + padrao.charAt(ii)) % Q;
            hashTexto = (D * hashTexto + texto.charAt(ii)) % Q;
        }

        for (int ii = 0; ii <= N - M; ii++) {
            if (hashPadrao == hashTexto) {
                if (padrao.equals(texto.substring(ii, ii + M))) {
                    return true;
                }
            }

            if (ii < N - M) {
                hashTexto = (D * (hashTexto - texto.charAt(ii) * h));
                hashTexto = (hashTexto + texto.charAt(ii + M)) % Q;

                if (hashTexto < 0) {
                    hashTexto = (hashTexto + Q);
                }
            }
        }
        return false;
    }
}
