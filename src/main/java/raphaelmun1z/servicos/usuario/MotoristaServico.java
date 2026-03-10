package raphaelmun1z.servicos.usuario;

import raphaelmun1z.entidades.usuario.Motorista;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Serviço de gerenciamento de Motoristas.
// Atua como um repositório em memória para centralizar o acesso aos dados da entidade.
public class MotoristaServico {

    // ESTRUTURA CHAVE: ArrayList
    // Escolhemos um ArrayList (lista dinâmica contígua em memória) pois o cenário
    // principal desta classe é o acesso sequencial rápido e a iteração constante
    // por outros serviços, sendo mais eficiente que uma LinkedList para leitura.
    private List<Motorista> motoristas = new ArrayList<>();

    // Massa de dados fictícia para testes e simulação de um ambiente de produção.
    // Essencial para termos dados suficientes para testar os algoritmos de busca,
    // ordenação e grafos do sistema sem depender de um banco de dados real.
    public void mockDados() {
        motoristas.add(new Motorista(1L, "João", "Silva", LocalDate.now(), 0));
        motoristas.add(new Motorista(2L, "Ânderson", "Rodrigues", LocalDate.now(), 0));
        motoristas.add(new Motorista(3L, "André", "Cardoso", LocalDate.now(), 0));
        motoristas.add(new Motorista(4L, "Édson", "Lopes", LocalDate.now(), 0));
        motoristas.add(new Motorista(5L, "Jorge", "de Sousa", LocalDate.now(), 0));
    }

    // Retorna a estrutura de dados completa.
    // Geralmente consumido por outros serviços que precisam varrer a lista
    // (como as buscas sequenciais ou alimentadores de tabelas hash/árvores).
    public List<Motorista> obterTodos() {
        return motoristas;
    }
}