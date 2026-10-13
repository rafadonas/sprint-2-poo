package br.com.motiva.dto;

import br.com.motiva.model.TipoClima;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TrechoRequest(
        @NotNull(message = "O km é obrigatório.")
        @PositiveOrZero(message = "O km não pode ser negativo.")
        Integer km,

        @NotNull(message = "A altura da vegetação é obrigatória.")
        @DecimalMin(value = "0.0", message = "A altura da vegetação não pode ser negativa.")
        @Digits(integer = 5, fraction = 2, message = "A altura deve ter até 5 inteiros e 2 decimais.")
        BigDecimal alturaVegetacao,

        @NotNull(message = "O clima é obrigatório.")
        TipoClima clima,

        @NotNull(message = "Informe se o trecho é monitorado.")
        Boolean monitorado) {
}
