package raphaelmun1z.servicos.usuario;

import raphaelmun1z.entidades.usuario.Passageiro;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PassageiroServico {

    // A escolha do ArrayList é fundamental aqui, pois a Busca Binária depende de
    // acesso indexado direto para pular para o meio da lista instantaneamente.
    private List<Passageiro> passageiros = new ArrayList<>();

    public void mockDados() {
        // Pré-requisito da Busca Binária: a base de dados precisa estar ordenada pela chave de busca (ID).
        passageiros.add(new Passageiro(1L, "Letícia", "Lopes", LocalDate.now(), 0));
        passageiros.add(new Passageiro(2L, "Adriana", "Carvalho", LocalDate.now(), 0));
        passageiros.add(new Passageiro(3L, "Adriana", "Dias", LocalDate.now(), 0));
        passageiros.add(new Passageiro(4L, "Simone", "Dias", LocalDate.now(), 0));
        passageiros.add(new Passageiro(5L, "Raimunda", "de Carvalho", LocalDate.now(), 0));
    }

    public List<Passageiro> obterTodos() {
        return passageiros;
    }

    // ==========================================
    // ALGORITMO: Busca Binária (Dividir para Conquistar)
    // OBJETIVO: Localizar um registro em bases de dados grandes descartando metade
    // das opções a cada iteração, tornando a busca absurdamente mais rápida que a sequencial.
    // ==========================================
    public Passageiro obterViaBuscaBinaria(Long id) {
        // Define os "ponteiros" que delimitam o espaço onde estamos procurando
        int inicio = 0;
        int fim = passageiros.size() - 1;

        // O laço continua até estreitarmos a busca a zero (ponteiros se cruzam)
        while (inicio <= fim) {

            // Encontra o índice central do espaço de busca atual.
            // O cálculo "inicio + (fim - inicio) / 2" evita o erro de "overflow" (estouro de memória)
            // que poderia acontecer em listas gigantescas se usássemos apenas "(inicio + fim) / 2".
            int meio = inicio + (fim - inicio) / 2;

            // Se o elemento do meio for exatamente o que queremos, a busca termina com sucesso
            if (passageiros.get(meio).getId().equals(id))
                return passageiros.get(meio);

            // Se o ID que buscamos é MAIOR que o ID do meio, sabemos com certeza que ele
            // não está na metade da esquerda. Então, ajustamos o ponteiro de início para
            // olhar apenas a metade da direita.
            if (passageiros.get(meio).getId() < id)
                inicio = meio + 1;

                // Se for MENOR, a lógica inverte: descartamos a direita e olhamos só para a esquerda.
            else
                fim = meio - 1;
        }

        // Se o laço terminar e os ponteiros se cruzarem, o espaço de busca acabou
        // e o registro definitivamente não existe na base.
        throw new IndexOutOfBoundsException("Passageiro não encontrado!");
    }
}