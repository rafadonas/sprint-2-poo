package model;

/** Classe abstrata mantida da Sprint 2 para os serviços operacionais. */
public abstract class IntervencaoOperacional {
    private final String tipo;

    protected IntervencaoOperacional(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public abstract String executarServico();
}
