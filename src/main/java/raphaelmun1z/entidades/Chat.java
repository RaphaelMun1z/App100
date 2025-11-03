package raphaelmun1z.entidades;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private Long id;
    private Motorista motorista;
    private Passageiro passageiro;
    private List<Mensagem> mensagens = new ArrayList<>();

    public Chat(Long id, Motorista motorista, Passageiro passageiro) {
        this.id = id;
        this.motorista = motorista;
        this.passageiro = passageiro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void addMensagem(Mensagem msg) {
        mensagens.add(msg);
    }

    @Override
    public String toString() {
        return "Chat #" + id +
            "\n Motorista: " + motorista +
            "\n Passageiro: " + passageiro +
            "\n ___________________________";
    }
}
