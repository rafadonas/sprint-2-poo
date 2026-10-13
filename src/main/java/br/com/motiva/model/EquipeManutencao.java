package br.com.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "EQUIPE_MANUTENCAO")
public class EquipeManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_equipe")
    @SequenceGenerator(
            name = "seq_equipe",
            sequenceName = "SEQ_EQUIPE_MANUTENCAO",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "ESPECIALIDADE", nullable = false, length = 100)
    private String especialidade;

    @Convert(converter = SimNaoBooleanConverter.class)
    @Column(name = "ATIVA", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private boolean ativa;

    protected EquipeManutencao() {
    }

    public EquipeManutencao(String nome, String especialidade, boolean ativa) {
        atualizar(nome, especialidade, ativa);
    }

    public void atualizar(String nome, String especialidade, boolean ativa) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.ativa = ativa;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public boolean isAtiva() {
        return ativa;
    }
}
