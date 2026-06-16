/**
 * Define os tipos de clima e seus fatores de crescimento da vegetação.
 */
public enum TipoClima {
    UMIDO(1.5),  // Cresce 50% mais rápido
    SECO(0.8),   // Cresce 20% mais devagar
    NORMAL(1.0);

    private final double fator;

    TipoClima(double fator) {
        this.fator = fator;
    }

    public double getFator() {
        return fator;
    }
}
