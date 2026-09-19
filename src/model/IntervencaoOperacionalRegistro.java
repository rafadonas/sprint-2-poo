package model;

import java.time.LocalDate;
import java.util.Objects;

/** Registro persistente de uma intervenção operacional. */
public record IntervencaoOperacionalRegistro(
        long id,
        long trechoId,
        long equipeId,
        String tipo,
        String descricao,
        LocalDate dataAgendada,
        StatusIntervencao status) {

    public IntervencaoOperacionalRegistro {
        if (id < 0 || trechoId <= 0 || equipeId <= 0) {
            throw new IllegalArgumentException("Os identificadores informados são inválidos.");
        }
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("O tipo da intervenção é obrigatório.");
        }
        Objects.requireNonNull(dataAgendada, "A data agendada é obrigatória.");
        Objects.requireNonNull(status, "O status é obrigatório.");
    }

    public IntervencaoOperacionalRegistro comId(long novoId) {
        return new IntervencaoOperacionalRegistro(
                novoId, trechoId, equipeId, tipo, descricao, dataAgendada, status);
    }
}
