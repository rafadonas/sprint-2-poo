/**
 * Representa o serviço de pulverização química.
 */
public class Pulverizacao extends IntervencaoOperacional {
    public Pulverizacao() {
        super("Pulverização");
    }

    @Override
    public String executarServico() {
        return "Executando pulverização.";
    }
}
