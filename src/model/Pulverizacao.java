package model;

/** Serviço de pulverização química e preventiva. */
public class Pulverizacao extends IntervencaoOperacional {
    public Pulverizacao() {
        super("Pulverização");
    }

    @Override
    public String executarServico() {
        return "Executando pulverização.";
    }
}
