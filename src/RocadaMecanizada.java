/**
 * Representa o serviço de roçada feita por máquinas.
 */
public class RocadaMecanizada extends IntervencaoOperacional {
    public RocadaMecanizada() {
        super("Roçada Mecanizada");
    }

    @Override
    public String executarServico() {
        return "Executando roçada mecanizada.";
    }
}
