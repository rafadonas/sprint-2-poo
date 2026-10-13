package br.com.motiva.dto;

import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.model.StatusIntervencao;

import java.time.LocalDate;

public record IntervencaoResponse(
        Long id,
        Long trechoId,
        Integer trechoKm,
        Long equipeId,
        String equipeNome,
        String tipo,
        String descricao,
        LocalDate dataAgendada,
        StatusIntervencao status) {

    public static IntervencaoResponse de(IntervencaoOperacional intervencao) {
        return new IntervencaoResponse(
                intervencao.getId(),
                intervencao.getTrecho().getId(),
                intervencao.getTrecho().getKm(),
                intervencao.getEquipe().getId(),
                intervencao.getEquipe().getNome(),
                intervencao.getTipo(),
                intervencao.getDescricao(),
                intervencao.getDataAgendada(),
                intervencao.getStatus());
    }
}
