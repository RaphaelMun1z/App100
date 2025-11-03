package raphaelmun1z.servicos;

import raphaelmun1z.entidades.Motorista;
import raphaelmun1z.entidades.TipoCorrida;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MotoristaServico {
    private List<Motorista> motoristas = new ArrayList<>();

    public void mockDados() {
        motoristas.add(new Motorista(1L, "João", "Silva", LocalDate.now(), 0));
        motoristas.add(new Motorista(2L, "Ânderson", "Rodrigues", LocalDate.now(), 0));
        motoristas.add(new Motorista(3L, "André", "Cardoso", LocalDate.now(), 0));
        motoristas.add(new Motorista(4L, "Édson", "Lopes", LocalDate.now(), 0));
        motoristas.add(new Motorista(5L, "Jorge", "de Sousa", LocalDate.now(), 0));
    }

    public List<Motorista> obterTodos() {
        return motoristas;
    }
}
