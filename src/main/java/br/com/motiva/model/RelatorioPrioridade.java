package br.com.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "RELATORIO_PRIORIDADE")
public class RelatorioPrioridade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_relatorio")
    @SequenceGenerator(
            name = "seq_relatorio",
            sequenceName = "SEQ_RELATORIO_PRIORIDADE",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATA_GERACAO", nullable = false)
    private LocalDateTime dataGeracao;

    @Column(name = "QT_URGENTE", nullable = false)
    private int quantidadeUrgente;

    @Column(name = "QT_CRITICO", nullable = false)
    private int quantidadeCritica;

    @Column(name = "QT_ATENCAO", nullable = false)
    private int quantidadeAtencao;

    @Column(name = "QT_NORMAL", nullable = false)
    private int quantidadeNormal;

    @Column(name = "RESUMO", nullable = false, length = 2000)
    private String resumo;

    protected RelatorioPrioridade() {
    }

    public RelatorioPrioridade(
            int quantidadeUrgente,
            int quantidadeCritica,
            int quantidadeAtencao,
            int quantidadeNormal,
            String resumo) {
        this.dataGeracao = LocalDateTime.now();
        this.quantidadeUrgente = quantidadeUrgente;
        this.quantidadeCritica = quantidadeCritica;
        this.quantidadeAtencao = quantidadeAtencao;
        this.quantidadeNormal = quantidadeNormal;
        this.resumo = resumo;
    }

    @PrePersist
    void preencherDataGeracao() {
        if (dataGeracao == null) {
            dataGeracao = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public int getQuantidadeUrgente() {
        return quantidadeUrgente;
    }

    public int getQuantidadeCritica() {
        return quantidadeCritica;
    }

    public int getQuantidadeAtencao() {
        return quantidadeAtencao;
    }

    public int getQuantidadeNormal() {
        return quantidadeNormal;
    }

    public String getResumo() {
        return resumo;
    }
}
