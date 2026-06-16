/**
 * Classe abstrata que representa uma intervenção na rodovia.
 */
public abstract class IntervencaoOperacional {
    private final String tipo;

    public IntervencaoOperacional(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    /**
     * Método abstrato para executar o serviço específico.
     */
    public abstract String executarServico();
}
