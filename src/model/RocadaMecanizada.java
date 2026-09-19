package model;

/** Serviço de roçada realizado por máquinas. */
public class RocadaMecanizada extends IntervencaoOperacional {
    public RocadaMecanizada() {
        super("Roçada Mecanizada");
    }

    @Override
    public String executarServico() {
        return "Executando roçada mecanizada.";
    }
}
