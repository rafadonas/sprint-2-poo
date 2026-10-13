package br.com.motiva.dto;

import br.com.motiva.model.StatusIntervencao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record IntervencaoRequest(
        @NotNull(message = "O trecho é obrigatório.")
        @Positive(message = "O identificador do trecho deve ser positivo.")
        Long trechoId,

        @NotNull(message = "A equipe é obrigatória.")
        @Positive(message = "O identificador da equipe deve ser positivo.")
        Long equipeId,

        @NotBlank(message = "O tipo da intervenção é obrigatório.")
        @Size(max = 80, message = "O tipo deve ter no máximo 80 caracteres.")
        String tipo,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
        String descricao,

        @NotNull(message = "A data agendada é obrigatória.")
        LocalDate dataAgendada,

        @NotNull(message = "O status é obrigatório.")
        StatusIntervencao status) {
}
