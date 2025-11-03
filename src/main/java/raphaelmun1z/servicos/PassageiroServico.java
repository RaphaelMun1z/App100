package raphaelmun1z.servicos;

import raphaelmun1z.entidades.Motorista;
import raphaelmun1z.entidades.Passageiro;
import raphaelmun1z.entidades.TipoCorrida;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PassageiroServico {
    private List<Passageiro> passageiros = new ArrayList<>();

    public void mockDados() {
        passageiros.add(new Passageiro(1L, "Letícia", "Lopes", LocalDate.now(), 0));
        passageiros.add(new Passageiro(2L, "Adriana", "Carvalho", LocalDate.now(), 0));
        passageiros.add(new Passageiro(3L, "Adriana", "Dias", LocalDate.now(), 0));
        passageiros.add(new Passageiro(4L, "Simone", "Dias", LocalDate.now(), 0));
        passageiros.add(new Passageiro(5L, "Raimunda", "de Carvalho", LocalDate.now(), 0));
    }

    public List<Passageiro> obterTodos() {
        return passageiros;
    }

    public Passageiro obterViaBuscaBinaria(Long id) {
        int inicio = 0;
        int fim = passageiros.size() - 1;

        while (inicio <= fim) {
            int meio = inicio + (fim - inicio) / 2;

            if (passageiros.get(meio).getId().equals(id))
                return passageiros.get(meio);

            if (passageiros.get(meio).getId() < id)
                inicio = meio + 1;
            else
                fim = meio - 1;
        }

        throw new IndexOutOfBoundsException("Passageiro não encontrado!");
    }
}
