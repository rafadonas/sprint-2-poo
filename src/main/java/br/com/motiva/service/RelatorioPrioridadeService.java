package br.com.motiva.service;

import br.com.motiva.dto.RelatorioResponse;
import br.com.motiva.exception.RegraNegocioException;
import br.com.motiva.model.RelatorioPrioridade;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.RelatorioPrioridadeRepository;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RelatorioPrioridadeService {

    private static final BigDecimal LIMITE_URGENTE = new BigDecimal("50.0");
    private static final BigDecimal LIMITE_CRITICO = new BigDecimal("40.0");
    private static final BigDecimal LIMITE_ATENCAO = new BigDecimal("30.0");

    private final RelatorioPrioridadeRepository relatorioRepository;
    private final TrechoRodoviaRepository trechoRepository;

    public RelatorioPrioridadeService(
            RelatorioPrioridadeRepository relatorioRepository,
            TrechoRodoviaRepository trechoRepository) {
        this.relatorioRepository = relatorioRepository;
        this.trechoRepository = trechoRepository;
    }

    @Transactional
    public RelatorioResponse gerar() {
        List<TrechoRodovia> trechos = trechoRepository.findAllByOrderByKmAsc();
        int urgente = 0;
        int critico = 0;
        int atencao = 0;
        int normal = 0;

        for (TrechoRodovia trecho : trechos) {
            switch (classificar(trecho.getAlturaVegetacao())) {
                case URGENTE -> urgente++;
                case CRITICO -> critico++;
                case ATENCAO -> atencao++;
                case NORMAL -> normal++;
            }
        }

        String resumo = "Total: %d | Urgente: %d | Crítico: %d | Atenção: %d | Normal: %d"
                .formatted(trechos.size(), urgente, critico, atencao, normal);
        RelatorioPrioridade relatorio = new RelatorioPrioridade(
                urgente, critico, atencao, normal, resumo);
        return RelatorioResponse.de(relatorioRepository.save(relatorio));
    }

    @Transactional(readOnly = true)
    public List<RelatorioResponse> listarHistorico() {
        return relatorioRepository.findAllByOrderByDataGeracaoDescIdDesc().stream()
                .map(RelatorioResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RelatorioResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new RegraNegocioException("As datas de início e fim são obrigatórias.");
        }
        if (inicio.isAfter(fim)) {
            throw new RegraNegocioException("A data inicial não pode ser posterior à data final.");
        }
        return relatorioRepository
                .findByDataGeracaoBetweenOrderByDataGeracaoDesc(inicio, fim)
                .stream()
                .map(RelatorioResponse::de)
                .toList();
    }

    private Prioridade classificar(BigDecimal alturaVegetacao) {
        if (alturaVegetacao.compareTo(LIMITE_URGENTE) > 0) {
            return Prioridade.URGENTE;
        }
        if (alturaVegetacao.compareTo(LIMITE_CRITICO) > 0) {
            return Prioridade.CRITICO;
        }
        if (alturaVegetacao.compareTo(LIMITE_ATENCAO) > 0) {
            return Prioridade.ATENCAO;
        }
        return Prioridade.NORMAL;
    }

    private enum Prioridade {
        URGENTE,
        CRITICO,
        ATENCAO,
        NORMAL
    }
}
