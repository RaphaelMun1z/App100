package raphaelmun1z.servicos;

import raphaelmun1z.entidades.TipoCorrida;

import java.util.ArrayList;
import java.util.List;

public class TipoCorridaServico {
    private List<TipoCorrida> tiposCorrida = new ArrayList<>();

    public void mockDados() {
        tiposCorrida.add(new TipoCorrida(1L, "100Pop", "Categoria mais popular e com o melhor custo-benefício"));
        tiposCorrida.add(new TipoCorrida(2L, "100Plus", "Para quem busca mais conforto, com carros mais espaçosos e motoristas bem avaliados."));
        tiposCorrida.add(new TipoCorrida(3L, "100Táxi", "Permite solicitar um táxi tradicional com a praticidade do aplicativo."));
        tiposCorrida.add(new TipoCorrida(4L, "100Negocia", "O passageiro pode negociar o preço da corrida diretamente com o motorista pelo aplicativo."));
        tiposCorrida.add(new TipoCorrida(5L, "100Moto", "Opção rápida e econômica para viagens em duas rodas."));
        tiposCorrida.add(new TipoCorrida(6L, "100Entrega", "Serviço para o transporte rápido de objetos e encomendas."));
        tiposCorrida.add(new TipoCorrida(7L, "100Eletric-Pro", "Categoria premium que oferece carros elétricos ou híbridos para uma viagem mais tecnológica e confortável."));
    }

    public List<TipoCorrida> obterTodos() {
        return tiposCorrida;
    }

    public TipoCorrida obterViaIdBuscaSequencial(Long id) {
        for (int ii = 0; ii < tiposCorrida.size(); ii++) {
            if (tiposCorrida.get(ii).getId().equals(id))
                return tiposCorrida.get(ii);
        }
        throw new IndexOutOfBoundsException("Tipo de Corrida não encontrada!");
    }
}
