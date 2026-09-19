package model;

/** Define os tipos de clima e seus fatores de crescimento da vegetação. */
public enum TipoClima {
    UMIDO(1.5),
    SECO(0.8),
    NORMAL(1.0);

    private final double fator;

    TipoClima(double fator) {
        this.fator = fator;
    }

    public double getFator() {
        return fator;
    }
}
