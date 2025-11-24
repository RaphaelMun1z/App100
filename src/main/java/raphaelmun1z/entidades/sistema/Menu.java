package raphaelmun1z.entidades.sistema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private String titulo;
    private Map<Integer, ItemMenu> opcoes = new LinkedHashMap<>();

    public Menu(String titulo) {
        this.titulo = titulo;
    }

    public void adicionarOpcao(int numero, String descricao, Runnable acao) {
        opcoes.put(numero, new ItemMenu(descricao, acao));
    }

    public void executar(Scanner scanner) {
        int opcao = -1;
        while (opcao != 0) {
            exibirCabecalho();
            try {
                System.out.print("Escolha uma opção: ");
                opcao = Integer.parseInt(scanner.nextLine());

                if (opcao == 0) {
                    IO.println("Saindo/Voltando...");
                    return;
                }

                ItemMenu item = opcoes.get(opcao);
                if (item != null) {
                    item.acao.run();
                    pausar(scanner);
                } else {
                    IO.println("Opção inválida!");
                    pausar(scanner);
                }
            } catch (NumberFormatException e) {
                IO.println("Digite apenas números.");
                opcao = -1;
            } catch (Exception e) {
                IO.println("Erro ao executar opção: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void exibirCabecalho() {
        IO.println("\n========================================");
        IO.println("         " + titulo);
        IO.println("========================================");
        for (Map.Entry<Integer, ItemMenu> entry : opcoes.entrySet()) {
            IO.println("[" + entry.getKey() + "] " + entry.getValue().descricao);
        }
        IO.println("[0] Sair / Voltar");
    }

    private void pausar(Scanner scanner) {
        IO.println("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
}
