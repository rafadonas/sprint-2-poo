package br.com.motiva.dto;

import br.com.motiva.model.TipoClima;
import br.com.motiva.model.TrechoRodovia;

import java.math.BigDecimal;

public record TrechoResponse(
        Long id,
        Integer km,
        BigDecimal alturaVegetacao,
        TipoClima clima,
        boolean monitorado) {

    public static TrechoResponse de(TrechoRodovia trecho) {
        return new TrechoResponse(
                trecho.getId(),
                trecho.getKm(),
                trecho.getAlturaVegetacao(),
                trecho.getClima(),
                trecho.isMonitorado());
    }
}
