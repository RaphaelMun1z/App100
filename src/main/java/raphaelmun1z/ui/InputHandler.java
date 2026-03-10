package raphaelmun1z.ui;

import raphaelmun1z.servicos.pd.CorrecaoEnderecoServico;
import raphaelmun1z.entidades.interfaces.IGrafo;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

// Utilitário para centralizar e tratar as entradas do usuário no terminal.
// Atua como um filtro entre o que o usuário digita e o que os algoritmos de grafo esperam receber.
public class InputHandler {
    private final Scanner scanner;
    private final CorrecaoEnderecoServico pd;

    public InputHandler(Scanner scanner, CorrecaoEnderecoServico pd) {
        this.scanner = scanner;
        this.pd = pd;
    }

    // Intercepta a leitura de cidades para aplicar o algoritmo de Levenshtein (Programação Dinâmica)
    // antes de repassar o dado. Isso garante resiliência: buscas no grafo não falham por simples erros de digitação.
    public String lerCidadeValidada(String mensagem, IGrafo grafo) {
        Set<String> cidades = new HashSet<>(grafo.obterCidades());

        // Laço de repetição: prende o usuário aqui até ele digitar algo que exista no mapa
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine();

            // Aciona a inteligência de Correção de Endereço baseada na distância de edição
            String sugerida = pd.sugerirCorrecao(entrada, cidades);

            // Feedback visual amigável: avisa o usuário caso o sistema tenha atuado para corrigir a entrada dele
            if (!sugerida.equalsIgnoreCase(entrada)) {
                System.out.println("-> [PD] Você quis dizer '" + sugerida + "'? Usando esta cidade.");
            }

            // TRAVA DE SEGURANÇA: Só devolve a cidade para o programa se ela realmente for válida.
            if (cidades.contains(sugerida)) {
                return sugerida;
            } else {
                // Se a cidade não existir e a PD não conseguir corrigir, bloqueia e pede de novo.
                System.out.println("-> [ERRO] A cidade '" + sugerida + "' não existe no mapa. Tente novamente.\n");
            }
        }
    }
}