package model;

/** Registro persistente de uma equipe de manutenção. */
public record EquipeManutencao(long id, String nome, String especialidade, boolean ativa) {
    public EquipeManutencao {
        if (id < 0) {
            throw new IllegalArgumentException("O id não pode ser negativo.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da equipe é obrigatório.");
        }
        if (especialidade == null || especialidade.isBlank()) {
            throw new IllegalArgumentException("A especialidade é obrigatória.");
        }
    }

    public EquipeManutencao comId(long novoId) {
        return new EquipeManutencao(novoId, nome, especialidade, ativa);
    }
}
