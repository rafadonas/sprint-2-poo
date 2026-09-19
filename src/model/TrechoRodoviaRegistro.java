package model;

import java.util.Objects;

/** DTO imutável usado pelo DAO de trechos. */
public record TrechoRodoviaRegistro(
        long id,
        int km,
        double alturaVegetacao,
        TipoClima clima,
        boolean monitorado) {

    public TrechoRodoviaRegistro {
        if (id < 0) {
            throw new IllegalArgumentException("O id não pode ser negativo.");
        }
        if (km < 0) {
            throw new IllegalArgumentException("O km não pode ser negativo.");
        }
        if (alturaVegetacao < 0) {
            throw new IllegalArgumentException("A altura da vegetação não pode ser negativa.");
        }
        Objects.requireNonNull(clima, "O clima é obrigatório.");
    }

    public TrechoRodoviaRegistro comId(long novoId) {
        return new TrechoRodoviaRegistro(novoId, km, alturaVegetacao, clima, monitorado);
    }

    public TrechoRodovia paraModelo() {
        if (monitorado) {
            return new TrechoRodoviaMonitorado(id, km, alturaVegetacao, clima);
        }
        return new TrechoRodovia(id, km, alturaVegetacao, clima, false);
    }

    public static TrechoRodoviaRegistro deModelo(TrechoRodovia trecho) {
        Objects.requireNonNull(trecho, "O trecho é obrigatório.");
        return new TrechoRodoviaRegistro(
                trecho.getId(), trecho.getKm(), trecho.getAlturaVegetacao(),
                trecho.getClima(), trecho.isMonitorado());
    }
}
