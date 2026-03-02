package raphaelmun1z.ui;

import raphaelmun1z.servicos.pd.CorrecaoEnderecoServico;
import raphaelmun1z.entidades.interfaces.IGrafo;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class InputHandler {
    private final Scanner scanner;
    private final CorrecaoEnderecoServico pd;

    public InputHandler(Scanner scanner, CorrecaoEnderecoServico pd) {
        this.scanner = scanner;
        this.pd = pd;
    }

    public String lerCidadeValidada(String mensagem, IGrafo grafo) {
        System.out.print(mensagem);
        String entrada = scanner.nextLine();

        Set<String> cidades = new HashSet<>(grafo.obterCidades());
        String sugerida = pd.sugerirCorrecao(entrada, cidades);

        if (!sugerida.equalsIgnoreCase(entrada)) {
            System.out.println("-> [PD] Você quis dizer '" + sugerida + "'? Usando esta cidade.");
        }
        return sugerida;
    }
}