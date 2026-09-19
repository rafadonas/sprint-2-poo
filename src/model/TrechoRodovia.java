package model;

import java.util.Objects;

/** Representa um trecho da rodovia com informações de vegetação. */
public class TrechoRodovia {
    private final long id;
    private final int km;
    protected double alturaVegetacao;
    private final TipoClima clima;
    private final boolean monitorado;

    public TrechoRodovia(int km, double alturaVegetacao, TipoClima clima) {
        this(0, km, alturaVegetacao, clima, false);
    }

    public TrechoRodovia(long id, int km, double alturaVegetacao, TipoClima clima,
                         boolean monitorado) {
        if (id < 0) {
            throw new IllegalArgumentException("O id não pode ser negativo.");
        }
        if (km < 0) {
            throw new IllegalArgumentException("O km não pode ser negativo.");
        }
        if (alturaVegetacao < 0) {
            throw new IllegalArgumentException("A altura da vegetação não pode ser negativa.");
        }
        this.id = id;
        this.km = km;
        this.alturaVegetacao = alturaVegetacao;
        this.clima = Objects.requireNonNull(clima, "O clima é obrigatório.");
        this.monitorado = monitorado;
    }

    /** Simula o crescimento da vegetação de acordo com o clima local. */
    public void crescer() {
        alturaVegetacao += 2.0 * clima.getFator();
    }

    public long getId() {
        return id;
    }

    public int getKm() {
        return km;
    }

    public double getAlturaVegetacao() {
        return alturaVegetacao;
    }

    public TipoClima getClima() {
        return clima;
    }

    public boolean isMonitorado() {
        return monitorado;
    }
}
