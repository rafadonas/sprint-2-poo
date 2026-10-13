package br.com.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "TRECHO_RODOVIA")
public class TrechoRodovia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trecho")
    @SequenceGenerator(
            name = "seq_trecho",
            sequenceName = "SEQ_TRECHO_RODOVIA",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "KM", nullable = false)
    private Integer km;

    @Column(name = "ALTURA_VEGETACAO", nullable = false, precision = 7, scale = 2)
    private BigDecimal alturaVegetacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "CLIMA", nullable = false, length = 10)
    private TipoClima clima;

    @Convert(converter = SimNaoBooleanConverter.class)
    @Column(name = "MONITORADO", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private boolean monitorado;

    protected TrechoRodovia() {
    }

    public TrechoRodovia(
            Integer km,
            BigDecimal alturaVegetacao,
            TipoClima clima,
            boolean monitorado) {
        atualizar(km, alturaVegetacao, clima, monitorado);
    }

    public void atualizar(
            Integer km,
            BigDecimal alturaVegetacao,
            TipoClima clima,
            boolean monitorado) {
        this.km = km;
        this.alturaVegetacao = alturaVegetacao;
        this.clima = clima;
        this.monitorado = monitorado;
    }

    /** Mantém o comportamento de crescimento criado na Sprint 1. */
    public void crescer() {
        BigDecimal acrescimo = BigDecimal.valueOf(2.0 * clima.getFator());
        alturaVegetacao = alturaVegetacao.add(acrescimo);
    }

    public Long getId() {
        return id;
    }

    public Integer getKm() {
        return km;
    }

    public BigDecimal getAlturaVegetacao() {
        return alturaVegetacao;
    }

    public TipoClima getClima() {
        return clima;
    }

    public boolean isMonitorado() {
        return monitorado;
    }
}
