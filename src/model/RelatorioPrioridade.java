package model;

import java.time.LocalDateTime;
import java.util.Objects;

/** Histórico imutável de um relatório de prioridades. */
public record RelatorioPrioridade(
        long id,
        LocalDateTime dataGeracao,
        int quantidadeUrgente,
        int quantidadeCritica,
        int quantidadeAtencao,
        int quantidadeNormal,
        String resumo) {

    public RelatorioPrioridade {
        if (id < 0 || quantidadeUrgente < 0 || quantidadeCritica < 0
                || quantidadeAtencao < 0 || quantidadeNormal < 0) {
            throw new IllegalArgumentException("Ids e quantidades não podem ser negativos.");
        }
        Objects.requireNonNull(dataGeracao, "A data de geração é obrigatória.");
        if (resumo == null || resumo.isBlank()) {
            throw new IllegalArgumentException("O resumo é obrigatório.");
        }
    }

    public RelatorioPrioridade comId(long novoId) {
        return new RelatorioPrioridade(
                novoId, dataGeracao, quantidadeUrgente, quantidadeCritica,
                quantidadeAtencao, quantidadeNormal, resumo);
    }
}
