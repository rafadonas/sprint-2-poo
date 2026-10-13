package br.com.motiva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipeRequest(
        @NotBlank(message = "O nome da equipe é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,

        @NotBlank(message = "A especialidade é obrigatória.")
        @Size(max = 100, message = "A especialidade deve ter no máximo 100 caracteres.")
        String especialidade,

        @NotNull(message = "Informe se a equipe está ativa.")
        Boolean ativa) {
}
