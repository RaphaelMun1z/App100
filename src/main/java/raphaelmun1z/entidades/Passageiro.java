package raphaelmun1z.entidades;

import java.time.LocalDate;

public class Passageiro extends Usuario {

    public Passageiro(Long id, String nome, String sobrenome, LocalDate dataNascimento, Integer qntCorridasRealizadas) {
        super(id, nome, sobrenome, dataNascimento, qntCorridasRealizadas);
    }

    @Override
    public String toString() {
        return "[Passageiro] " + this.getNome() + " " + this.getSobrenome() + " - Corridas Realizadas: " + this.getQntCorridasRealizadas();
    }
}
