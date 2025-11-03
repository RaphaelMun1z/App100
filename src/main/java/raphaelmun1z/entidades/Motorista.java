package raphaelmun1z.entidades;

import java.time.LocalDate;

public class Motorista extends Usuario {

    public Motorista(Long id, String nome, String sobrenome, LocalDate dataNascimento, Integer qntCorridasRealizadas) {
        super(id, nome, sobrenome, dataNascimento, qntCorridasRealizadas);
    }

    @Override
    public String toString() {
        return "[Motorista] " + this.getNome() + " " + this.getSobrenome() + " - Corridas Realizadas: " + this.getQntCorridasRealizadas();
    }
}
