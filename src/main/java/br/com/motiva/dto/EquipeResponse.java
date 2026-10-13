package br.com.motiva.dto;

import br.com.motiva.model.EquipeManutencao;

public record EquipeResponse(Long id, String nome, String especialidade, boolean ativa) {

    public static EquipeResponse de(EquipeManutencao equipe) {
        return new EquipeResponse(
                equipe.getId(),
                equipe.getNome(),
                equipe.getEspecialidade(),
                equipe.isAtiva());
    }
}
