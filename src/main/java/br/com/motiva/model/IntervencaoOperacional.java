package br.com.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "INTERVENCAO_OPERACIONAL")
public class IntervencaoOperacional {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_intervencao")
    @SequenceGenerator(
            name = "seq_intervencao",
            sequenceName = "SEQ_INTERVENCAO_OPERACIONAL",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRECHO_ID", nullable = false)
    private TrechoRodovia trecho;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EQUIPE_ID", nullable = false)
    private EquipeManutencao equipe;

    @Column(name = "TIPO", nullable = false, length = 80)
    private String tipo;

    @Column(name = "DESCRICAO", length = 500)
    private String descricao;

    @Column(name = "DATA_AGENDADA", nullable = false)
    private LocalDate dataAgendada;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private StatusIntervencao status;

    protected IntervencaoOperacional() {
    }

    public IntervencaoOperacional(
            TrechoRodovia trecho,
            EquipeManutencao equipe,
            String tipo,
            String descricao,
            LocalDate dataAgendada,
            StatusIntervencao status) {
        atualizar(trecho, equipe, tipo, descricao, dataAgendada, status);
    }

    public void atualizar(
            TrechoRodovia trecho,
            EquipeManutencao equipe,
            String tipo,
            String descricao,
            LocalDate dataAgendada,
            StatusIntervencao status) {
        this.trecho = trecho;
        this.equipe = equipe;
        this.tipo = tipo;
        this.descricao = descricao;
        this.dataAgendada = dataAgendada;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public TrechoRodovia getTrecho() {
        return trecho;
    }

    public EquipeManutencao getEquipe() {
        return equipe;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataAgendada() {
        return dataAgendada;
    }

    public StatusIntervencao getStatus() {
        return status;
    }
}
