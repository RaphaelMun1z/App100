package raphaelmun1z.entidades;

import java.time.LocalDate;

public abstract class Usuario {
    private Long id;
    private String nome;
    private String sobrenome;
    private LocalDate dataNascimento;
    private Integer qntCorridasRealizadas;

    public Usuario(Long id, String nome, String sobrenome, LocalDate dataNascimento, Integer qntCorridasRealizadas) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNascimento = dataNascimento;
        this.qntCorridasRealizadas = qntCorridasRealizadas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Integer getQntCorridasRealizadas() {
        return qntCorridasRealizadas;
    }

    public void setQntCorridasRealizadas(Integer qntCorridasRealizadas) {
        this.qntCorridasRealizadas = qntCorridasRealizadas;
    }

    @Override
    public String toString() {
        return "#" + this.getId() +
            "\n" + this.getNome() + " " +
            this.getSobrenome() +
            " - Corridas Realizadas: " +
            this.getQntCorridasRealizadas();
    }
}
