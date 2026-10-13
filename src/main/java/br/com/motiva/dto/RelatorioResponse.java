package br.com.motiva.dto;

import br.com.motiva.model.RelatorioPrioridade;

import java.time.LocalDateTime;

public record RelatorioResponse(
        Long id,
        LocalDateTime dataGeracao,
        int quantidadeUrgente,
        int quantidadeCritica,
        int quantidadeAtencao,
        int quantidadeNormal,
        String resumo) {

    public static RelatorioResponse de(RelatorioPrioridade relatorio) {
        return new RelatorioResponse(
                relatorio.getId(),
                relatorio.getDataGeracao(),
                relatorio.getQuantidadeUrgente(),
                relatorio.getQuantidadeCritica(),
                relatorio.getQuantidadeAtencao(),
                relatorio.getQuantidadeNormal(),
                relatorio.getResumo());
    }
}
